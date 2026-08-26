import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProcessadorLogs {

    static class PedacoLog {
        private final int id;
        private final List<String> linhas;
        private final boolean finalizar;

        public PedacoLog(int id, List<String> linhas, boolean finalizar) {
            this.id = id;
            this.linhas = linhas;
            this.finalizar = finalizar;
        }

        public int getId() {
            return id;
        }

        public List<String> getLinhas() {
            return linhas;
        }

        public boolean isFinalizar() {
            return finalizar;
        }
    }

    static class ResumoParcial {
        private final int idPedaco;
        private final int idTrabalhador;
        private final Map<Integer, Integer> contagemErros;
        private final int linhasInvalidas;

        public ResumoParcial(int idPedaco, int idTrabalhador, Map<Integer, Integer> contagemErros, int linhasInvalidas) {
            this.idPedaco = idPedaco;
            this.idTrabalhador = idTrabalhador;
            this.contagemErros = contagemErros;
            this.linhasInvalidas = linhasInvalidas;
        }

        public int getIdPedaco() {
            return idPedaco;
        }

        public int getIdTrabalhador() {
            return idTrabalhador;
        }

        public Map<Integer, Integer> getContagemErros() {
            return contagemErros;
        }

        public int getLinhasInvalidas() {
            return linhasInvalidas;
        }
    }

    static class Trabalhador implements Runnable {
        private final int id;
        private final BlockingQueue<PedacoLog> filaPedacos;
        private final BlockingQueue<ResumoParcial> filaResultados;

        public Trabalhador(int id, BlockingQueue<PedacoLog> filaPedacos, BlockingQueue<ResumoParcial> filaResultados) {
            this.id = id;
            this.filaPedacos = filaPedacos;
            this.filaResultados = filaResultados;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    PedacoLog pedaco = filaPedacos.take();

                    if (pedaco.isFinalizar()) {
                        break;
                    }

                    ResumoParcial resumo = processarPedaco(pedaco);
                    filaResultados.put(resumo);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private ResumoParcial processarPedaco(PedacoLog pedaco) {
            // Cada trabalhador possui sua própria contagem local.
            Map<Integer, Integer> contagemLocal = new HashMap<>();
            int linhasInvalidas = 0;

            for (String linha : pedaco.getLinhas()) {
                String[] campos = linha.split(",", -1);

                if (campos.length != 4) {
                    linhasInvalidas++;
                    continue;
                }

                try {
                    int codigoErro = Integer.parseInt(campos[2].trim());

                    if (codigoErro < 0 || codigoErro > 3) {
                        linhasInvalidas++;
                        continue;
                    }

                    contagemLocal.merge(codigoErro, 1, Integer::sum);

                } catch (NumberFormatException e) {
                    linhasInvalidas++;
                }
            }

            System.out.println("Trabalhador " + id + " terminou o pedaço " + pedaco.getId());

            return new ResumoParcial(pedaco.getId(), id, contagemLocal, linhasInvalidas);
        }
    }

    public static void main(String[] args) {
        String nomeArquivo = args.length > 0 ? args[0] : "erro.log";
        int quantidadeTrabalhadores = 4;
        int linhasPorPedaco = 50;

        BlockingQueue<PedacoLog> filaPedacos = new LinkedBlockingQueue<>();
        BlockingQueue<ResumoParcial> filaResultados = new LinkedBlockingQueue<>();

        // Pool com quatro trabalhadores.
        ExecutorService pool = Executors.newFixedThreadPool(quantidadeTrabalhadores);

        for (int i = 1; i <= quantidadeTrabalhadores; i++) {
            Trabalhador trabalhador = new Trabalhador(i, filaPedacos, filaResultados);
            pool.execute(trabalhador);
        }

        try {
            int quantidadePedacos = lerArquivoEEnviarPedacos(nomeArquivo, linhasPorPedaco, filaPedacos);

            System.out.println();
            System.out.println("Quantidade de pedaços enviados: " + quantidadePedacos);
            System.out.println();

            // Uma mensagem de finalização para cada trabalhador.
            for (int i = 0; i < quantidadeTrabalhadores; i++) {
                filaPedacos.put(new PedacoLog(-1, new ArrayList<>(), true));
            }

            Map<Integer, Integer> resultadoFinal = new TreeMap<>();
            int totalLinhasInvalidas = 0;

            // Coordenador recebe e junta os resultados parciais.
            for (int i = 0; i < quantidadePedacos; i++) {
                ResumoParcial resumo = filaResultados.take();

                System.out.println(
                    "Resultado recebido: Trabalhador " +
                    resumo.getIdTrabalhador() +
                    " | Pedaço " +
                    resumo.getIdPedaco() +
                    " | " +
                    resumo.getContagemErros()
                );

                for (Map.Entry<Integer, Integer> entrada : resumo.getContagemErros().entrySet()) {
                    resultadoFinal.merge(entrada.getKey(), entrada.getValue(), Integer::sum);
                }

                totalLinhasInvalidas += resumo.getLinhasInvalidas();
            }

            exibirResultadoFinal(resultadoFinal, totalLinhasInvalidas);

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Processamento interrompido.");

        } finally {
            pool.shutdown();

            try {
                pool.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static int lerArquivoEEnviarPedacos(String nomeArquivo, int linhasPorPedaco, BlockingQueue<PedacoLog> filaPedacos) throws IOException, InterruptedException {
        int idPedaco = 1;
        List<String> linhas = new ArrayList<>(linhasPorPedaco);

        // BufferedReader evita carregar o arquivo inteiro na memória.
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                linhas.add(linha);

                if (linhas.size() == linhasPorPedaco) {
                    PedacoLog pedaco = new PedacoLog(idPedaco, new ArrayList<>(linhas), false);
                    filaPedacos.put(pedaco);

                    System.out.println("Coordenador enviou o pedaço " + idPedaco + " com " + linhas.size() + " linhas.");

                    idPedaco++;
                    linhas.clear();
                }
            }

            if (!linhas.isEmpty()) {
                PedacoLog ultimoPedaco = new PedacoLog(idPedaco, new ArrayList<>(linhas), false);
                filaPedacos.put(ultimoPedaco);

                System.out.println("Coordenador enviou o pedaço " + idPedaco + " com " + linhas.size() + " linhas.");

                idPedaco++;
            }
        }

        return idPedaco - 1;
    }

    private static String retornarDescricaoErro(int codigo) {
        switch (codigo) {
            case 0:
                return "Erro de conexão com o banco";
            case 1:
                return "Erro na montagem de volume";
            case 2:
                return "Erro na execução da migrate";
            case 3:
                return "Erro no buffer de memória";
            default:
                return "Erro desconhecido";
        }
    }

    private static void exibirResultadoFinal(Map<Integer, Integer> resultadoFinal, int linhasInvalidas) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("          RESULTADO FINAL");
        System.out.println("========================================");

        int totalErros = 0;

        for (int codigo = 0; codigo <= 3; codigo++) {
            int quantidade = resultadoFinal.getOrDefault(codigo, 0);
            totalErros += quantidade;

            System.out.println(
                "Código " +
                codigo +
                " - " +
                retornarDescricaoErro(codigo) +
                ": " +
                quantidade +
                " ocorrência(s)"
            );
        }

        System.out.println("----------------------------------------");
        System.out.println("Total de erros processados: " + totalErros);
        System.out.println("Linhas inválidas: " + linhasInvalidas);
        System.out.println("========================================");
    }
}
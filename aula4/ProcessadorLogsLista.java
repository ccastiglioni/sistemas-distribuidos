import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessadorLogsLista {

    static class ResumoParcial {
        private final int idPedaco;
        private final Map<Integer, Integer> contagemErros;

        public ResumoParcial(int idPedaco, Map<Integer, Integer> contagemErros) {
            this.idPedaco = idPedaco;
            this.contagemErros = contagemErros;
        }

        public int getIdPedaco() {
            return idPedaco;
        }

        public Map<Integer, Integer> getContagemErros() {
            return contagemErros;
        }
    }

    static class Trabalhador implements Callable<ResumoParcial> {
        private final int idPedaco;
        private final List<String> linhas;

        public Trabalhador(int idPedaco, List<String> linhas) {
            this.idPedaco = idPedaco;
            this.linhas = linhas;
        }

        @Override
        public ResumoParcial call() {
            Map<Integer, Integer> contagemLocal = new HashMap<>();

            for (String linha : linhas) {
                String[] campos = linha.split(",");

                if (campos.length != 4) {
                    continue;
                }

                try {
                    int codigoErro = Integer.parseInt(campos[2].trim());

                    if (codigoErro >= 0 && codigoErro <= 3) {
                        contagemLocal.merge(codigoErro, 1, Integer::sum);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Linha inválida: " + linha);
                }
            }

            System.out.println("Pedaço " + idPedaco + " processado.");

            return new ResumoParcial(idPedaco, contagemLocal);
        }
    }

    public static void main(String[] args) {
        String nomeArquivo = "erro.log";
        int quantidadeTrabalhadores = 4;
        int linhasPorPedaco = 50;

        ExecutorService pool = Executors.newFixedThreadPool(quantidadeTrabalhadores);

        try {
            List<List<String>> pedacos = dividirArquivo(nomeArquivo, linhasPorPedaco);
            List<Future<ResumoParcial>> resultados = new ArrayList<>();

            for (int i = 0; i < pedacos.size(); i++) {
                Trabalhador trabalhador = new Trabalhador(i + 1, pedacos.get(i));
                resultados.add(pool.submit(trabalhador));
            }

            Map<Integer, Integer> resultadoFinal = new TreeMap<>();

            for (Future<ResumoParcial> futuro : resultados) {
                ResumoParcial resumo = futuro.get();

                System.out.println(
                    "Resultado do pedaço " +
                    resumo.getIdPedaco() +
                    ": " +
                    resumo.getContagemErros()
                );

                for (Map.Entry<Integer, Integer> entrada : resumo.getContagemErros().entrySet()) {
                    resultadoFinal.merge(entrada.getKey(), entrada.getValue(), Integer::sum);
                }
            }

            exibirResultadoFinal(resultadoFinal);

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Erro no processamento: " + e.getMessage());

        } finally {
            pool.shutdown();
        }
    }

    private static List<List<String>> dividirArquivo(String nomeArquivo, int linhasPorPedaco) throws IOException {
        List<List<String>> pedacos = new ArrayList<>();
        List<String> pedacoAtual = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                pedacoAtual.add(linha);

                if (pedacoAtual.size() == linhasPorPedaco) {
                    pedacos.add(new ArrayList<>(pedacoAtual));
                    pedacoAtual.clear();
                }
            }

            if (!pedacoAtual.isEmpty()) {
                pedacos.add(new ArrayList<>(pedacoAtual));
            }
        }

        return pedacos;
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

    private static void exibirResultadoFinal(Map<Integer, Integer> resultadoFinal) {
        System.out.println();
        System.out.println("===== RESULTADO FINAL =====");

        int totalErros = 0;

        for (int codigo = 0; codigo <= 3; codigo++) {
            int quantidade = resultadoFinal.getOrDefault(codigo, 0);
            totalErros += quantidade;

            System.out.println(
                codigo + " - " +
                retornarDescricaoErro(codigo) +
                ": " +
                quantidade
            );
        }

        System.out.println("Total de erros: " + totalErros);
    }
}
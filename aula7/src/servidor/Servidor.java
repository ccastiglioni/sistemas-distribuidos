package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Sessao;
import model.TabelaHash;
import utilidades.Util;

public class Servidor {
    private static final int PORTA_PADRAO = 5000;
    private static final int MAX_CLIENTES_SIMULTANEOS = 10;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java -cp out servidor.Servidor <arquivo.txt> [porta]");
            return;
        }

        final int porta;
        try {
            porta = args.length >= 2 ? Integer.parseInt(args[1]) : PORTA_PADRAO;
        } catch (NumberFormatException e) {
            System.err.println("A porta precisa ser um número inteiro.");
            return;
        }

        TabelaHash tabela = new TabelaHash();
        try {
            int totalLinhas = Util.carregarArquivoEmHashMap(Path.of(args[0]), tabela.sessoes);
            System.out.printf("Base carregada: %d registros, %d celulares distintos.%n",
                    totalLinhas, tabela.sessoes.size());
        } catch (IOException e) {
            System.err.println("Erro ao ler base de sessões: " + e.getMessage());
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES_SIMULTANEOS);
        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.printf("Servidor TCP ativo na porta %d. Aguardando clientes...%n", porta);
            while (true) {
                Socket cliente = servidor.accept(); // Bloqueante apenas nesta thread principal.
                pool.execute(() -> atenderCliente(cliente, tabela));
            }
        } catch (IOException e) {
            System.err.println("Erro de rede no servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void atenderCliente(Socket socket, TabelaHash tabela) {
        System.out.println("Cliente conectado: " + socket.getRemoteSocketAddress());
        try (Socket cliente = socket;
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(cliente.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true,
                     StandardCharsets.UTF_8)) {

            cliente.setSoTimeout(120_000); // 2 minutos sem receber uma linha.
            String numeroCelular;
            while ((numeroCelular = entrada.readLine()) != null) {
                numeroCelular = numeroCelular.trim();
                if (numeroCelular.isEmpty()) {
                    saida.println("ERRO;Informe um número de celular");
                    continue;
                }
                Sessao sessao = tabela.sessoes.get(numeroCelular);
                if (sessao == null) {
                    saida.println("ERRO;Número de celular não encontrado");
                } else {
                    // Protocolo de resposta: OK;<dados_da_sessão>
                    saida.println("OK;" + sessao.getDadosSessao());
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Cliente desconectado por inatividade.");
        } catch (IOException e) {
            System.err.println("Conexão encerrada: " + e.getMessage());
        } finally {
            System.out.println("Cliente desconectado: " + socket.getRemoteSocketAddress());
        }
    }
}

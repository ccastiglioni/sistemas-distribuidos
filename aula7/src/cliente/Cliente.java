package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Cliente {
    public static void main(String[] args) {
        String host = args.length >= 1 ? args[0] : "127.0.0.1";
        final int porta;
        try {
            porta = args.length >= 2 ? Integer.parseInt(args[1]) : 5000;
        } catch (NumberFormatException e) {
            System.err.println("A porta precisa ser um número inteiro.");
            return;
        }

        try (Socket socket = new Socket(host, porta);
             BufferedReader teclado = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true,
                     StandardCharsets.UTF_8)) {

            System.out.printf("Conectado ao servidor %s:%d%n", host, porta);
            System.out.println("Informe o número de celular, ou 'sair' para encerrar.");
            while (true) {
                System.out.print("Celular> ");
                System.out.flush();
                String numero = teclado.readLine();
                if (numero == null || numero.trim().equalsIgnoreCase("sair")) break;
                if (numero.isBlank()) continue;

                saida.println(numero.trim()); // Envia a requisição pela rede TCP.
                String resposta = entrada.readLine(); // Aguarda a resposta.
                if (resposta == null) {
                    System.out.println("Servidor encerrou a conexão.");
                    break;
                }
                if (resposta.startsWith("OK;")) {
                    System.out.println("Sessão encontrada: " + resposta.substring(3));
                } else if (resposta.startsWith("ERRO;")) {
                    System.out.println(resposta.substring(5));
                } else {
                    System.out.println("Resposta inesperada: " + resposta);
                }
            }
            System.out.println("Cliente finalizado.");
        } catch (IOException e) {
            System.err.println("Não foi possível comunicar com o servidor: " + e.getMessage());
        }
    }
}

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

    public static void main(String[] args) {

        String enderecoServidor = "127.0.0.1";
        int portaServidor = 12345;

        Scanner teclado = new Scanner(System.in);

        try {

            DatagramSocket socketCliente = new DatagramSocket();

            System.out.print("Digite seu nome: ");
            String nome = teclado.nextLine();

            System.out.print("Digite seu email: ");
            String email = teclado.nextLine();

            String mensagem = nome + ";" + email;

            byte[] dadosEnvio = mensagem.getBytes();

            InetAddress endereco =
                    InetAddress.getByName(enderecoServidor);

            DatagramPacket pacoteEnvio =
                    new DatagramPacket(
                            dadosEnvio,
                            dadosEnvio.length,
                            endereco,
                            portaServidor
                    );

            socketCliente.send(pacoteEnvio);

            System.out.println("\nDados enviados ao servidor.");

            byte[] bufferResposta = new byte[1024];

            DatagramPacket pacoteResposta =
                    new DatagramPacket(
                            bufferResposta,
                            bufferResposta.length
                    );

            socketCliente.receive(pacoteResposta);

            String resposta = new String(
                    pacoteResposta.getData(),
                    0,
                    pacoteResposta.getLength()
            );

            System.out.println("Servidor respondeu: " + resposta);

            socketCliente.close();

        } catch (Exception e) {

            System.out.println("Erro no cliente: " + e.getMessage());
        }
    }
}

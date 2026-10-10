import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class ServidorUDP {

    public static void main(String[] args) {

        int porta = 12345;

        List<String> pessoas = new ArrayList<>();

        try {

            DatagramSocket socketServidor = new DatagramSocket(porta);

            System.out.println("Servidor UDP iniciado na porta " + porta);

            while (true) {

                byte[] bufferRecebimento = new byte[1024];

                DatagramPacket pacoteRecebido =
                        new DatagramPacket(bufferRecebimento, bufferRecebimento.length);

                System.out.println("\nAguardando cliente...");

                socketServidor.receive(pacoteRecebido);

                String mensagem = new String(
                        pacoteRecebido.getData(),
                        0,
                        pacoteRecebido.getLength()
                );

                System.out.println("Mensagem recebida: " + mensagem);

                String resposta;

                if (pessoas.contains(mensagem)) {

                    resposta = "Cliente ja cadastrado";

                } else {

                    pessoas.add(mensagem);

                    resposta = "Cadastro realizado com sucesso";
                }

                System.out.println("Clientes cadastrados:");
                System.out.println(pessoas);

                byte[] dadosResposta = resposta.getBytes();

                DatagramPacket pacoteResposta =
                        new DatagramPacket(
                                dadosResposta,
                                dadosResposta.length,
                                pacoteRecebido.getAddress(),
                                pacoteRecebido.getPort()
                        );

                socketServidor.send(pacoteResposta);
            }

        } catch (Exception e) {

            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
}

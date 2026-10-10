import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Comunicador implements AutoCloseable {
    public static final String ENDERECO_SERVIDOR = "127.0.0.1";
    public static final int PORTA_SERVIDOR = 12345;
    public static final int TAMANHO_MAXIMO_MENSAGEM = 1024;
    public static final int TEMPO_LIMITE_RESPOSTA_MS = 5000;

    private final DatagramSocket socket;

    public Comunicador(int porta) throws SocketException {
        socket = new DatagramSocket(porta);
    }

    public Comunicador(InetAddress endereco, int porta) throws SocketException {
        socket = new DatagramSocket();

        try {
            socket.connect(endereco, porta);
            socket.setSoTimeout(TEMPO_LIMITE_RESPOSTA_MS);
        } catch (SocketException | RuntimeException e) {
            socket.close();
            throw e;
        }
    }

    public void enviar(String mensagem, InetAddress endereco, int porta) throws IOException {
        byte[] dados = mensagem.getBytes(StandardCharsets.UTF_8);

        if (dados.length > TAMANHO_MAXIMO_MENSAGEM) {
            throw new IOException("A mensagem excede o limite de " + TAMANHO_MAXIMO_MENSAGEM + " bytes.");
        }

        DatagramPacket pacote = new DatagramPacket(dados, dados.length, endereco, porta);
        socket.send(pacote);
    }

    public DatagramPacket receber() throws IOException {
        byte[] buffer = new byte[TAMANHO_MAXIMO_MENSAGEM + 1];
        DatagramPacket pacote = new DatagramPacket(buffer, buffer.length);
        socket.receive(pacote);
        return pacote;
    }

    public static String lerMensagem(DatagramPacket pacote) {
        return new String(pacote.getData(), pacote.getOffset(), pacote.getLength(), StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
        socket.close();
    }
}

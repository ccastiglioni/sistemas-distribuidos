import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

public class Cliente {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int porta = 8001;
        ArrayList<Pessoa> pessoas = new ArrayList<>();

        while (true) {
            String nome = JOptionPane.showInputDialog(null, "Nome completo");
            if (nome == null) {
                break;
            }

            String email = JOptionPane.showInputDialog(null, "Email");
            if (email == null) {
                break;
            }

            try {
                Socket socket = new Socket("localhost", porta);

                ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
                saida.flush();
                saida.writeObject(new Pessoa(nome, email));

                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                pessoas = (ArrayList<Pessoa>) entrada.readObject();

                entrada.close();
                saida.close();
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Erro ao comunicar com o servidor: " + e.getMessage());
                continue;
            }

            // toda vez que uma lista é recebida, reordena pelo nome antes de exibir
            pessoas.sort(Comparator.comparing(Pessoa::getNome, String.CASE_INSENSITIVE_ORDER));

            StringBuilder texto = new StringBuilder("Pessoas cadastradas:\n");
            for (Pessoa p : pessoas) {
                texto.append(p).append("\n");
            }
            JOptionPane.showMessageDialog(null, texto.toString());
        }

        System.out.println("Cliente encerrado.");
    }
}

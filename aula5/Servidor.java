import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Servidor {
    public static void main(String[] args) {
        try {
            int porta = 8001;
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Servidor ouvindo a porta: " + porta);

            ArrayList<Pessoa> cadastrados = new ArrayList<>();

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());

                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                Pessoa pessoa = (Pessoa) entrada.readObject();

                boolean jaCadastrado = false;
                for (Pessoa p : cadastrados) {
                    if (p.getNome().trim().equalsIgnoreCase(pessoa.getNome().trim())) {
                        jaCadastrado = true;
                        break;
                    }
                }

                if (jaCadastrado) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Pessoa já cadastrada: " + pessoa.getNome()
                    );
                } else {
                    cadastrados.add(pessoa);
                    System.out.println("Cadastrado: " + pessoa);
                }

                // devolve a lista atual para o cliente atualizar sua própria cópia
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(cadastrados);

                saida.close();
                entrada.close();
                cliente.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}

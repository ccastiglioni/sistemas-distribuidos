import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ClienteUDP {

    private static final int ESPACAMENTO = 10;
    private static final int COLUNAS_CAMPO = 30;
    private static final int LINHAS_RESPOSTA = 3;

    private final JTextField campoNome = new JTextField(COLUNAS_CAMPO);
    private final JTextField campoEmail = new JTextField(COLUNAS_CAMPO);
    private final JButton botaoCadastrar = new JButton("Cadastrar");
    private final JTextArea resultado = new JTextArea(LINHAS_RESPOSTA, COLUNAS_CAMPO);

    public void exibir() {
        JFrame janela = new JFrame("Cadastro de cliente via UDP");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(0, 1, ESPACAMENTO, ESPACAMENTO));
        formulario.add(new JLabel("Nome completo"));
        formulario.add(campoNome);
        formulario.add(new JLabel("E-mail"));
        formulario.add(campoEmail);
        formulario.add(botaoCadastrar);

        resultado.setEditable(false);
        resultado.setLineWrap(true);
        resultado.setWrapStyleWord(true);
        resultado.setText("Informe seus dados para realizar o cadastro.");

        JPanel conteudo = new JPanel(new BorderLayout(ESPACAMENTO, ESPACAMENTO));
        conteudo.setBorder(BorderFactory.createEmptyBorder(
                ESPACAMENTO, ESPACAMENTO, ESPACAMENTO, ESPACAMENTO));
        conteudo.add(formulario, BorderLayout.CENTER);
        conteudo.add(resultado, BorderLayout.SOUTH);
        janela.setContentPane(conteudo);
        janela.getRootPane().setDefaultButton(botaoCadastrar);

        botaoCadastrar.addActionListener(evento -> cadastrar());
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    private void cadastrar() {
        final Pessoa pessoa;
        try {
            pessoa = new Pessoa(campoNome.getText(), campoEmail.getText());
        } catch (IllegalArgumentException e) {
            resultado.setText(e.getMessage());
            return;
        }

        botaoCadastrar.setEnabled(false);
        campoNome.setEnabled(false);
        campoEmail.setEnabled(false);
        resultado.setText("Aguardando resposta do servidor...");

        // O recebimento UDP pode bloquear; o SwingWorker mantém a janela responsiva.
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws IOException {
                InetAddress endereco = InetAddress.getByName(Comunicador.ENDERECO_SERVIDOR);
                try (Comunicador comunicador = new Comunicador(endereco, Comunicador.PORTA_SERVIDOR)) {
                    comunicador.enviar(pessoa.getNome() + ";" + pessoa.getEmail(),
                            endereco, Comunicador.PORTA_SERVIDOR);
                    return Comunicador.lerMensagem(comunicador.receber());
                }
            }

            @Override
            protected void done() {
                try {
                    resultado.setText(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    resultado.setText("Cadastro interrompido.");
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof SocketTimeoutException) {
                        resultado.setText("O servidor não respondeu a tempo. Tente novamente.");
                    } else if (e.getCause() instanceof PortUnreachableException) {
                        resultado.setText("Servidor indisponível. Verifique se ele está em execução.");
                    } else {
                        resultado.setText("Não foi possível cadastrar: " + e.getCause().getMessage());
                    }
                } finally {
                    botaoCadastrar.setEnabled(true);
                    campoNome.setEnabled(true);
                    campoEmail.setEnabled(true);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClienteUDP().exibir());
    }
}

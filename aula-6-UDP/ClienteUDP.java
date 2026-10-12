import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class ClienteUDP {

    private static final int ESPACAMENTO = 10;
    private static final int COLUNAS_CAMPO = 30;
    private static final int LINHAS_RESPOSTA = 3;
    private static final Duration INTERVALO_SOLICITACAO_TOKEN = Duration.ofSeconds(10);
    private static final DateTimeFormatter FORMATO_HORARIO =
            DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    private final JTextField campoNome = new JTextField(COLUNAS_CAMPO);
    private final JTextField campoEmail = new JTextField(COLUNAS_CAMPO);
    private final JButton botaoCadastrar = new JButton("Cadastrar");
    private final JTextArea resultado = new JTextArea(LINHAS_RESPOSTA, COLUNAS_CAMPO);
    private final JTextField campoToken = new JTextField(COLUNAS_CAMPO);
    private final JLabel validadeToken = new JLabel("Cadastre-se para receber um token.");
    private final JButton botaoToken = new JButton("Solicitar token");
    private final Timer consultaPeriodica = new Timer(
            (int) INTERVALO_SOLICITACAO_TOKEN.toMillis(), evento -> solicitarToken());
    private final Timer expiracaoToken = new Timer(0, evento -> {
        campoToken.setText("");
        validadeToken.setText("Token expirado. Aguardando nova consulta.");
    });

    private String emailCadastrado;
    private boolean requisicaoEmAndamento;

    public ClienteUDP() {
        campoToken.setEditable(false);
        botaoToken.setEnabled(false);
        expiracaoToken.setRepeats(false);
    }

    public void exibir() {
        JFrame janela = new JFrame("Cadastro de cliente via UDP");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {
                interromperTokens();
            }
        });

        JPanel formulario = new JPanel(new GridLayout(0, 1, ESPACAMENTO, ESPACAMENTO));
        formulario.add(new JLabel("Nome completo"));
        formulario.add(campoNome);
        formulario.add(new JLabel("E-mail"));
        formulario.add(campoEmail);
        formulario.add(botaoCadastrar);
        formulario.add(new JLabel("Token do cliente (consulta a cada "
                + INTERVALO_SOLICITACAO_TOKEN.toSeconds() + " segundos)"));
        formulario.add(campoToken);
        formulario.add(validadeToken);
        formulario.add(botaoToken);

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
        botaoToken.addActionListener(evento -> solicitarToken());
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
    }

    private void cadastrar() {
        if (requisicaoEmAndamento) {
            return;
        }

        final Pessoa pessoa;
        try {
            pessoa = new Pessoa(campoNome.getText(), campoEmail.getText());
        } catch (IllegalArgumentException e) {
            resultado.setText(e.getMessage());
            return;
        }

        interromperTokens();
        enviarRequisicao(pessoa.getNome() + ";" + pessoa.getEmail(), resposta -> {
            resultado.setText(resposta);
            if (resposta.equals(Comunicador.CADASTRO_REALIZADO)
                    || resposta.equals(Comunicador.CLIENTE_JA_CADASTRADO)) {
                emailCadastrado = pessoa.getEmail();
                consultaPeriodica.start();
                // Aguarda a liberação dos controles ao concluir o cadastro.
                SwingUtilities.invokeLater(this::solicitarToken);
            }
        });
    }

    private void solicitarToken() {
        if (emailCadastrado == null || requisicaoEmAndamento) {
            return;
        }

        enviarRequisicao(Comunicador.COMANDO_TOKEN + ";" + emailCadastrado, resposta -> {
            if (resposta.equals(Comunicador.CLIENTE_NAO_CADASTRADO)) {
                interromperTokens();
                resultado.setText(resposta);
                return;
            }

            String[] campos = resposta.split(";", -1);
            if (campos.length != 3 || !campos[0].equals(Comunicador.COMANDO_TOKEN)) {
                resultado.setText("Resposta de token inválida. Uma nova consulta será feita automaticamente.");
                return;
            }

            final Instant expiraEm;
            try {
                UUID.fromString(campos[1]);
                expiraEm = Instant.ofEpochMilli(Long.parseLong(campos[2]));
            } catch (IllegalArgumentException e) {
                resultado.setText("O servidor retornou um token ou vencimento inválido.");
                return;
            }

            Instant agora = Instant.now();
            if (!agora.isBefore(expiraEm)) {
                expiracaoToken.stop();
                campoToken.setText("");
                validadeToken.setText("Token expirado. Aguardando nova consulta.");
                resultado.setText("O token recebido já expirou. Uma nova consulta será feita automaticamente.");
                return;
            }

            campoToken.setText(campos[1]);
            validadeToken.setText("Válido até " + FORMATO_HORARIO.format(expiraEm));
            // A mesma chave mantém o vencimento original, inclusive na interface.
            Duration tempoRestante = Duration.between(agora, expiraEm);
            if (tempoRestante.compareTo(Pessoa.VALIDADE_TOKEN) > 0) {
                tempoRestante = Pessoa.VALIDADE_TOKEN;
            }
            expiracaoToken.setInitialDelay((int) tempoRestante.toMillis());
            expiracaoToken.restart();
            resultado.setText("Token recebido. As consultas continuam a cada "
                    + INTERVALO_SOLICITACAO_TOKEN.toSeconds() + " segundos.");
        });
    }

    private void interromperTokens() {
        consultaPeriodica.stop();
        expiracaoToken.stop();
        emailCadastrado = null;
        campoToken.setText("");
        validadeToken.setText("Cadastre-se para receber um token.");
        botaoToken.setEnabled(false);
    }

    private void enviarRequisicao(String mensagem, Consumer<String> aoReceber) {
        requisicaoEmAndamento = true;
        botaoCadastrar.setEnabled(false);
        botaoToken.setEnabled(false);
        campoNome.setEnabled(false);
        campoEmail.setEnabled(false);
        resultado.setText("Aguardando resposta do servidor...");

        // O recebimento UDP pode bloquear; o SwingWorker mantém a janela responsiva.
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws IOException {
                InetAddress endereco = InetAddress.getByName(Comunicador.ENDERECO_SERVIDOR);
                try (Comunicador comunicador = new Comunicador(endereco, Comunicador.PORTA_SERVIDOR)) {
                    comunicador.enviar(mensagem, endereco, Comunicador.PORTA_SERVIDOR);
                    return Comunicador.lerMensagem(comunicador.receber());
                }
            }

            @Override
            protected void done() {
                try {
                    aoReceber.accept(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    resultado.setText("Solicitação interrompida.");
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof SocketTimeoutException) {
                        resultado.setText("O servidor não respondeu a tempo. Tente novamente.");
                    } else if (e.getCause() instanceof PortUnreachableException) {
                        resultado.setText("Servidor indisponível. Verifique se ele está em execução.");
                    } else {
                        resultado.setText("Falha na comunicação: " + e.getCause().getMessage());
                    }
                } finally {
                    requisicaoEmAndamento = false;
                    botaoCadastrar.setEnabled(true);
                    campoNome.setEnabled(true);
                    campoEmail.setEnabled(true);
                    botaoToken.setEnabled(emailCadastrado != null);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClienteUDP().exibir());
    }
}

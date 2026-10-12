import java.io.IOException;
import java.net.DatagramPacket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ServidorUDP {

    public static void main(String[] args) {

        List<Pessoa> pessoas = new ArrayList<>();

        try (Comunicador comunicador = new Comunicador(Comunicador.PORTA_SERVIDOR)) {

            System.out.println("Servidor UDP iniciado na porta " + Comunicador.PORTA_SERVIDOR);

            while (true) {

                System.out.println("\nAguardando cliente...");
                DatagramPacket pacoteRecebido = comunicador.receber();
                String resposta;

                try {
                    if (pacoteRecebido.getLength() > Comunicador.TAMANHO_MAXIMO_MENSAGEM) {
                        throw new IllegalArgumentException("Mensagem de cadastro muito grande.");
                    }

                    String mensagem = Comunicador.lerMensagem(pacoteRecebido);
                    String[] campos = mensagem.split(";", -1);
                    if (campos.length != 2) {
                        throw new IllegalArgumentException("Envie o cadastro no formato nome;e-mail.");
                    }

                    if (Comunicador.COMANDO_TOKEN.equals(campos[0])) {
                        String email = campos[1].strip();
                        Pessoa pessoa = null;
                        for (Pessoa cadastrada : pessoas) {
                            if (cadastrada.getEmail().equalsIgnoreCase(email)) {
                                pessoa = cadastrada;
                                break;
                            }
                        }

                        if (pessoa == null) {
                            resposta = Comunicador.CLIENTE_NAO_CADASTRADO;
                        } else {
                            String token = pessoa.solicitarToken(Instant.now());
                            resposta = Comunicador.COMANDO_TOKEN + ";" + token + ";"
                                    + pessoa.getTokenExpiraEm().toEpochMilli();
                        }
                    } else {
                        Pessoa pessoa = new Pessoa(campos[0], campos[1]);
                        boolean jaCadastrado = false;
                        for (Pessoa cadastrada : pessoas) {
                            if (cadastrada.getEmail().equals(pessoa.getEmail())) {
                                jaCadastrado = true;
                                break;
                            }
                        }

                        if (jaCadastrado) {
                            resposta = Comunicador.CLIENTE_JA_CADASTRADO;
                        } else {
                            pessoas.add(pessoa);
                            resposta = Comunicador.CADASTRO_REALIZADO;
                            System.out.println("Cadastrado: " + pessoa);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    resposta = "Cadastro inválido: " + e.getMessage();
                }

                comunicador.enviar(resposta, pacoteRecebido.getAddress(), pacoteRecebido.getPort());
            }

        } catch (IOException e) {

            System.out.println("Erro no servidor: " + e.getMessage());
        }
    }
}

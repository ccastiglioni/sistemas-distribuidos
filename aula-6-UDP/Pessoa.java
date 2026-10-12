import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Pessoa {
    public static final Duration VALIDADE_TOKEN = Duration.ofSeconds(60);

    private final String nome;
    private final String email;
    private String token;
    private Instant tokenExpiraEm;

    public Pessoa(String nome, String email) {
        if (nome == null || email == null) {
            throw new IllegalArgumentException("Informe o nome completo e o e-mail.");
        }

        String nomeNormalizado = nome.strip();
        String emailNormalizado = email.strip().toLowerCase(Locale.ROOT);

        if (nomeNormalizado.isEmpty() || emailNormalizado.isEmpty()) {
            throw new IllegalArgumentException("Informe o nome completo e o e-mail.");
        }

        if (nomeNormalizado.contains(";") || emailNormalizado.contains(";")) {
            throw new IllegalArgumentException("O nome e o e-mail não podem conter ponto e vírgula.");
        }

        if (!nomeNormalizado.matches("(?U)\\S+(?:\\s+\\S+)+")) {
            throw new IllegalArgumentException("Informe o nome completo, incluindo o sobrenome.");
        }

        if (!emailNormalizado.matches("(?U)[^\\s@;]+@[^\\s@;.]+(?:\\.[^\\s@;.]+)+")) {
            throw new IllegalArgumentException("Informe um e-mail válido, como nome@exemplo.com.");
        }

        this.nome = nomeNormalizado;
        this.email = emailNormalizado;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String solicitarToken(Instant agora) {
        Objects.requireNonNull(agora, "Informe o instante da solicitação.");

        if (token == null || !agora.isBefore(tokenExpiraEm)) {
            token = UUID.randomUUID().toString();
            tokenExpiraEm = agora.plus(VALIDADE_TOKEN);
        }

        return token;
    }

    public Instant getTokenExpiraEm() {
        return tokenExpiraEm;
    }

    @Override
    public String toString() {
        return nome + " - " + email;
    }
}

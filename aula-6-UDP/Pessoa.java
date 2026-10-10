import java.util.Locale;

public class Pessoa {
    private final String nome;
    private final String email;

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

    @Override
    public String toString() {
        return nome + " - " + email;
    }
}

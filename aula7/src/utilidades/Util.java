package utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import model.Sessao;

public class Util {
    // Executado uma vez, ANTES de aceitar clientes; depois o HashMap é somente leitura.
    public static int carregarArquivoEmHashMap(Path arquivo, HashMap<String, Sessao> sessoes)
            throws IOException {
        int linhasValidas = 0;
        try (BufferedReader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank()) continue;

                // Apenas o primeiro ';' separa celular dos dados da sessão (JSON).
                String[] partes = linha.split(";", 2);
                if (partes.length != 2 || partes[0].isBlank() || partes[1].isBlank()) {
                    System.err.println("Registro ignorado (formato inválido): " + linha);
                    continue;
                }
                String numero = partes[0].trim();
                sessoes.put(numero, new Sessao(numero, partes[1].trim()));
                linhasValidas++;
            }
        }
        return linhasValidas;
    }
}

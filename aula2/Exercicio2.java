import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Esta classe guarda a única lista usada pelas duas threads.
class ListaCompartilhada {
    private final List<Integer> numeros = new ArrayList<>();

    // synchronized permite que apenas uma thread adicione um número por vez.
    public synchronized void adicionarNumero(int numero) {
        numeros.add(numero);

        System.out.println(
            Thread.currentThread().getName() + " adicionou o número " + numero
        );
    }

    // Retorna uma cópia para ninguém alterar diretamente a lista protegida.
    public synchronized List<Integer> getNumeros() {
        return new ArrayList<>(numeros);
    }
}

// Cada objeto desta classe representa uma thread que lê um arquivo.
class ThreadLeitura extends Thread {
    private final String nomeArquivo;
    private final ListaCompartilhada listaCompartilhada;

    public ThreadLeitura(
        String nomeThread,
        String nomeArquivo,
        ListaCompartilhada listaCompartilhada
    ) {
        super(nomeThread);
        this.nomeArquivo = nomeArquivo;
        this.listaCompartilhada = listaCompartilhada;
    }

    @Override
    public void run() {
        // O arquivo fica na mesma pasta das classes e é aberto como recurso.
        InputStream arquivo = Exercicio2.class.getResourceAsStream(nomeArquivo);

        if (arquivo == null) {
            System.out.println("Arquivo não encontrado: " + nomeArquivo);
            return;
        }

        Scanner leitor = new Scanner(arquivo);

        // Cada linha do arquivo possui um número inteiro.
        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine();
            int numero = Integer.parseInt(linha);
            listaCompartilhada.adicionarNumero(numero);
        }

        leitor.close();
        System.out.println(Thread.currentThread().getName() + " terminou a leitura.");
    }
}

public class Exercicio2 {
    public static void main(String[] args) throws InterruptedException {
        // Este objeto será compartilhado pelas duas threads.
        ListaCompartilhada listaCompartilhada = new ListaCompartilhada();

        // Cada thread recebe um arquivo diferente, mas recebe a mesma lista.
        Thread threadArquivo1 = new ThreadLeitura(
            "Thread do arquivo 1",
            "/numeros1.txt",
            listaCompartilhada
        );

        Thread threadArquivo2 = new ThreadLeitura(
            "Thread do arquivo 2",
            "/numeros2.txt",
            listaCompartilhada
        );

        // Inicia as duas leituras.
        threadArquivo1.start();
        threadArquivo2.start();

        // A thread principal espera as duas leituras terminarem.
        threadArquivo1.join();
        threadArquivo2.join();

        // A lista só é exibida depois que os dois arquivos foram lidos.
        System.out.println("Lista final: " + listaCompartilhada.getNumeros());
    }
}

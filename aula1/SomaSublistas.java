import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Cada objeto desta classe recebe uma parte da lista e calcula sua soma.
class ThreadSoma extends Thread {
    private final List<Integer> sublista;
    private int somaParcial;

    public ThreadSoma(String nomeThread, List<Integer> sublista) {
        super(nomeThread);
        this.sublista = sublista;
        this.somaParcial = 0;
    }

    @Override
    public void run() {
        // Cada thread percorre somente a sublista recebida no construtor.
        for (Integer numero : sublista) {
            somaParcial = somaParcial + numero;
        }

        System.out.println(
            Thread.currentThread().getName()
                + " terminou com a soma parcial: "
                + somaParcial
        );
    }

    // A thread principal usa este método para obter o resultado parcial.
    public int getSomaParcial() {
        return somaParcial;
    }
}

public class SomaSublistas {
    private static final int QUANTIDADE_NUMEROS = 10_000;
    private static final int QUANTIDADE_THREADS = 4;
    private static final int LIMITE_NUMERO_ALEATORIO = 100;

    public static void main(String[] args) throws InterruptedException {
        List<Integer> numeros = new ArrayList<>();
        Random gerador = new Random();

        // Popula a lista principal com 10.000 números entre 1 e 100.
        for (int i = 0; i < QUANTIDADE_NUMEROS; i++) {
            int numeroAleatorio = gerador.nextInt(LIMITE_NUMERO_ALEATORIO) + 1;
            numeros.add(numeroAleatorio);
        }

        // Como 10.000 é divisível por 4, cada parte terá 2.500 números.
        int tamanhoParte = QUANTIDADE_NUMEROS / QUANTIDADE_THREADS;

        // Cada nova ArrayList é independente das demais.
        List<Integer> primeiraParte = new ArrayList<>(
            numeros.subList(0, tamanhoParte)
        );

        List<Integer> segundaParte = new ArrayList<>(
            numeros.subList(tamanhoParte, tamanhoParte * 2)
        );

        List<Integer> terceiraParte = new ArrayList<>(
            numeros.subList(tamanhoParte * 2, tamanhoParte * 3)
        );

        List<Integer> quartaParte = new ArrayList<>(
            numeros.subList(tamanhoParte * 3, QUANTIDADE_NUMEROS)
        );

        // Cria uma thread para cada uma das quatro partes.
        ThreadSoma thread1 = new ThreadSoma("Thread 1", primeiraParte);
        ThreadSoma thread2 = new ThreadSoma("Thread 2", segundaParte);
        ThreadSoma thread3 = new ThreadSoma("Thread 3", terceiraParte);
        ThreadSoma thread4 = new ThreadSoma("Thread 4", quartaParte);

        // Inicia as quatro somas concomitantemente.
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        // A thread principal aguarda o término das quatro threads.
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        // Soma os quatro resultados somente depois que todas terminaram.
        int somaTotal = thread1.getSomaParcial()
            + thread2.getSomaParcial()
            + thread3.getSomaParcial()
            + thread4.getSomaParcial();

        System.out.println("Quantidade de números: " + numeros.size());
        System.out.println("Quantidade em cada sublista: " + tamanhoParte);
        System.out.println("Soma total calculada pelas threads: " + somaTotal);
    }
}

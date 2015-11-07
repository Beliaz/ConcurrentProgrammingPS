package at.uibk.ac.at;

/**
 * Created by Florian on 07.11.2015.
 */
public class Task1a {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[8];

        for(int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                System.out.println(Thread.currentThread().getId());
            });

            threads[i].start();
        }

        for(Thread th : threads)
            th.join();
    }
}

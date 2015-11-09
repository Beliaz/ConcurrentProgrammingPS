package at.uibk.ac.at;

/**
 * Created by Florian on 07.11.2015.
 */
public class Task1b {
    public static void main(String[] args)
    {
        int count = 0;

        while(true) {
            new Thread(()->{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Thread count: " + ++count);
        }
    }
}

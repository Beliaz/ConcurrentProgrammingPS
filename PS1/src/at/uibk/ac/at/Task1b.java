package at.uibk.ac.at;

/**
 * Created by Florian on 07.11.2015.
 */
public class Task1b {
    public static void main(String[] args)
    {
        int count = 0;

        try
        {
            while(true) {
                new Thread(()->{
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

                count++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Thread count: " + count);
    }
}

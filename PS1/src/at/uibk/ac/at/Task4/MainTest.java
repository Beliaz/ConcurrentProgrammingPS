package at.uibk.ac.at.Task4;

import java.io.IOException;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class MainTest
{

    public static void main(String[] args) throws InterruptedException, IOException
    {
        System.out.println("Start test[Consumer/Producer]");
        System.out.println("Press enter to exit...");

        Producer producer = new Producer();
        Consumer consumer = new Consumer(producer);

        producer.start();
        consumer.start();

        consumer.join();

        System.out.println("Test finished...");
    }
}

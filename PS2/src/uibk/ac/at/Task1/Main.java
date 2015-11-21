package uibk.ac.at.Task1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Main
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        System.out.println("Start test[Consumer/Producer]");
        System.out.println("Press enter to exit...");

        int producerIndex = 1;

        List<Producer> producers = new LinkedList<Producer>();
        producers.add(new Producer("Producer " + (producerIndex++)));
        producers.add(new Producer("Producer " + (producerIndex++)));
        producers.add(new Producer("Producer " + (producerIndex++)));
        producers.add(new Producer("Producer " + (producerIndex++)));

        Consumer consumer = new Consumer(producers);

        for(Producer producer : producers)
            producer.start();

        consumer.start();

        consumer.join();

        System.out.println("Test finished...");
    }
}

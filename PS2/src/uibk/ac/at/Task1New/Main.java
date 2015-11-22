package uibk.ac.at.Task1New;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by flori on 22.11.2015.
 */
public class Main {
    public static void main(String[] args)
    {
        System.out.println("Start test[Consumer/Producer]");
        System.out.println("Press enter to exit...");

        List<Thread> threads = new LinkedList<>();
        List<Producer> producers = new LinkedList<Producer>();

        producers.add(new Producer("Producer " + producers.size()));
        producers.add(new Producer("Producer " + producers.size()));
        producers.add(new Producer("Producer " + producers.size()));
        producers.add(new Producer("Producer " + producers.size()));

        Consumer consumer = new Consumer("Consumer 0");
        threads.add(new Thread(consumer));

        Consumer anotherConsumer = new Consumer("Consumer 1");
        threads.add(new Thread(anotherConsumer));

        anotherConsumer.getBroker().addProducer(producers.get(0));
        anotherConsumer.getBroker().addProducer(producers.get(1));

        for (Producer producer : producers)
        {
            consumer.getBroker().addProducer(producer);
            threads.add(new Thread(producer));
        }

        threads.forEach(java.lang.Thread::start);

        for (Thread th : threads)
        {
            try
            {
                th.join();
            }
            catch (InterruptedException e) { }
        }

        System.out.println("Test finished...");
    }
}

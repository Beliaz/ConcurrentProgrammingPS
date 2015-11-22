package uibk.ac.at.Task1;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Consumer extends Thread
{
    private List<Producer> mProducers;

    public Consumer(List<Producer> producers)
    {
        super();
        mProducers = producers;
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            List<Pair<Producer, Integer>> valuePairs = new LinkedList<Pair<Producer, Integer>>();
            for(Producer producer : mProducers)
            {
                try
                {
                    valuePairs.add(new Pair<>(producer, producer.consume()));
                }
                catch (InterruptedException e)
                {
                    return;
                }
            }

            for(Pair<Producer, Integer> valuePair : valuePairs)
            {
                Producer producer = valuePair.key;
                Integer value = valuePair.value;

                System.out.println("[Consumer] consumed " + value);

                if(value == 0)
                {
                    producer.interrupt();
                    mProducers.remove(producer);
                    System.out.println("[Consumer] producer " + producer.getName() + " removed");

                    if(mProducers.isEmpty())
                    {
                        System.out.println("[Consumer] finished");
                        return;
                    }
                }
            }
        }
    }
}

package uibk.ac.at.Task1New;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by flori on 22.11.2015.
 */
public class Broker
{
    private Map<Producer, Integer> producers = new HashMap<>();;
    private Consumer consumer;

    public Broker(Consumer consumer)
    {
        this.consumer = consumer;
    }

    public synchronized void addProducer(Producer producer)
    {
        producers.put(producer, 0);
        producer.registerBroker(this);
    }

    public synchronized void offer(Producer producer)
    {
        int offers = producers.get(producer);
        producers.put(producer, ++offers);

        System.out.format("[Broker] for %s: received offer from %s\n", consumer.getName(), producer.getName());

        notifyAll();
    }

    public synchronized boolean demand()
    {
        if(producers.size() == 0)
            return false;

        System.out.format("[Broker] for %s: consumer demands goods...\n", consumer.getName());

        while(getDistinctOffers() < producers.size())
        {
            try
            {
                wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public synchronized List<Integer> getGoods()
    {
        List<Producer> toRemove = new LinkedList<>();
        List<Integer> goods = new LinkedList<>();

        for(Map.Entry<Producer, Integer> entry : producers.entrySet())
        {
            Producer producer = entry.getKey();
            Integer offers = entry.getValue();

            Integer item = producer.consume(this);

            if(item == 0)
            {
                toRemove.add(producer);
            }

            goods.add(item);

            producers.put(producer, --offers);
        }

        for(Producer producer : toRemove)
        {
            producer.unregister(this);
            producers.remove(producer);
            System.out.format("[Broker] producer %s removed\n", producer.getName());
        }

        return goods;
    }

    private int getDistinctOffers()
    {
        int distinctOffers = 0;

        for(Map.Entry<Producer, Integer> entry : producers.entrySet()) {
            distinctOffers += entry.getValue() > 0 ? 1 : 0;
        }

        return distinctOffers;
    }
}

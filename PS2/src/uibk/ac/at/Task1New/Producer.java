package uibk.ac.at.Task1New;

import java.util.*;

class Offer
{
    private final Broker broker;
    private final Queue<Integer> buffer;

    public Offer(Broker broker)
    {
        this.broker = broker;
        this.buffer = new LinkedList<>();
    }

    public Broker getBroker() { return broker; }
    public Queue<Integer> getGoods(){ return buffer; }
}

/**
 * Created by flori on 22.11.2015.
 */
public class Producer implements Runnable
{
    private final String name;
    private final Queue<Offer> offers = new LinkedList<>();
    private final Random random = new Random();

    private static final int maxItemCount = 10;
    private static final int maxValue = 3;
    private static final int maxSleepTime = 3000;

    Producer(String name)
    {
        this.name = name;
    }

    public Integer consume(Broker broker)
    {
        Offer targetOffer = null;

        synchronized (offers)
        {
            for(Offer offer : offers)
            {
                if(offer.getBroker().equals(broker))
                {
                    targetOffer = offer;
                    break;
                }
            }
        }

        if(targetOffer != null)
        {
            synchronized (targetOffer.getGoods())
            {
                return targetOffer.getGoods().remove();
            }
        }

        return -1;
    }

    public boolean produce()
    {
        Offer offer = null;
        synchronized (offers)
        {
            offer = offers.remove();
            offers.add(offer);
        }

        if(offer.getGoods().size() > maxItemCount) return false;

        Integer value = random.nextInt(maxValue);

        System.out.format("[Producer] %s: produced %d\n", getName(), value);

        synchronized (offer)
        {
            offer.getGoods().add(value);
            offer.getBroker().offer(this);
        }

        return true;
    }

    private void sleep() throws InterruptedException
    {
        //wait up to 3 Seconds
        int sleepTime = random.nextInt(maxSleepTime);
        System.out.format("[Producer] %s: waiting %.3f seconds\n", getName(), sleepTime / 1000.0);
        Thread.sleep(sleepTime);
    }

    public String getName() {
        return name;
    }

    public void registerBroker(Broker broker)
    {
        synchronized (offers) {
            offers.add(new Offer(broker));
        }
    }

    public void unregister(Broker broker)
    {
        synchronized (offers)
        {
            Iterator<Offer> it = offers.iterator();
            while(it.hasNext())
            {
                if(it.next().getBroker().equals(broker))
                {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void run()
    {
        while(offers.size() > 0)
        {
            try
            {
                if(produce())
                {
                    sleep();
                }
                else
                {
                    Thread.sleep(10);
                }
            }
            catch (InterruptedException e)
            {
                return;
            }
        }
    }
}

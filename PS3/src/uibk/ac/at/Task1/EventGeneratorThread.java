package uibk.ac.at.Task1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by flori on 06.12.2015.
 */
public class EventGeneratorThread extends Thread implements EventSource {

    private ThreadLocal<ArrayList<EventListener>> listeners = new ThreadLocal<>();
    private ConcurrentLinkedQueue<EventListener> toRegister= new ConcurrentLinkedQueue<>();

    private AtomicBoolean printList = new AtomicBoolean(false);

    @Override
    public void run()
    {
        final long timeBetweenEventsMs = 3 * 1000;
        final int sleepTimeMs = 100;

        listeners = new ThreadLocal<>();
        listeners.set(new ArrayList<>());

        while(!Thread.currentThread().isInterrupted())
        {
            long start = System.nanoTime();
            while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < timeBetweenEventsMs)
            {
                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    return;
                }

                if(printList.compareAndSet(true, false))
                {
                    final StringBuilder builder = new StringBuilder();
                    builder.append("Generator Thread[").append(getId()).append("] listeners:\n");
                    for(EventListener listener : listeners.get())
                    {
                        builder.append(" - ").append(listener.toString()).append("\n");
                    }

                    System.out.print(builder.append("\n").toString());
                }
            }

            if(!toRegister.isEmpty())
            {
                int count = toRegister.size();
                for(int i = 0; i < count; i++)
                {
                    listeners.get().add(toRegister.remove());
                }
            }

            final Event e = new MyEvent();
            for(EventListener listener : listeners.get())
            {
                listener.onEvent(e);
            }
        }
    }

    @Override
    public void registerListener(EventListener e)
    {
        toRegister.add(e);
    }

    public void infoListeningObjects()
    {
        printList.set(true);
    }
}

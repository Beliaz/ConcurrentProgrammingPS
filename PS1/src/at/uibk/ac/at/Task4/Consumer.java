package at.uibk.ac.at.Task4;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Consumer extends Thread
{
    private Producer mProducer;

    public Consumer(Producer producer)
    {
        super();
        mProducer = producer;
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            Integer val = mProducer.tryConsume();

            //Empty wait 2 seconds
            if(val == null)
            {
                System.out.println("[Consumer] empty buffer");
                System.out.println("[Consumer] wait 2 sec");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            else
            {
                System.out.println("[Consumer] consumed " + val);
                if(val == 0)
                {
                    System.out.println("[Consumer] got 0, exiting now...");
                    mProducer.interrupt();
                    return;
                }
            }
        }
    }
}

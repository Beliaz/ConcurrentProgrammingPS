package at.uibk.ac.at.Task1.cancellation_threads;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Generates random integer values from 0 to 41 <br>
 * Cancellation via {@link #cancel() cancel()} <br>
 * When canceled POIION_PILL is produced
 */
public class Thread1 extends Thread
{
    BlockingQueue<Integer> mQueue;
    boolean mRunning;

    /**
     * @param queue Produced values are added to this queue
     */
    public Thread1(BlockingQueue<Integer> queue)
    {
        mQueue = queue;
        mRunning = true;
    }

    @Override
    public void run()
    {
        while (running())
        {
            try
            {
                int val = new Random().nextInt(41);
                System.out.println("Thread1 generated:" + val);

                mQueue.put(val);
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
                System.out.println("Thread1 interrupted");
                System.out.println("Thread1 exiting");
                return;
            }
        }

        System.out.println("Thread1 canceled");
        System.out.println("Thread1 exiting");

        try {
            mQueue.put(PoisonPill.POIION_PILL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return;
    }

    /**
     * Provides information if the thread is running
     * @return Returns true for running and false if canceled
     */
    public synchronized boolean running() { return mRunning; }

    /**
     * Stop thread
     */
    public synchronized void cancel() { mRunning = false; }
}
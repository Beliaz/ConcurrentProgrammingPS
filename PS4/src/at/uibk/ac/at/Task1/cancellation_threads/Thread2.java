package at.uibk.ac.at.Task1.cancellation_threads;

import java.util.concurrent.BlockingQueue;

/**
 * Consumes values from the <strong>in</strong> and forwards even values to <strong>out</strong> <br>
 * Stops when POIION_PILL is consumed
 */
public class Thread2 extends Thread
    {
        BlockingQueue<Integer> mQueueIn;
        BlockingQueue<Integer> mQueueOut;

        /**
         * @param in Inputqueue
         * @param out Outputqueue
         */
        public Thread2(BlockingQueue<Integer> in, BlockingQueue<Integer> out)
        {
            mQueueIn = in;
            mQueueOut = out;
        }

        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    //Forward to thread 3
                    int val = mQueueIn.take();

                    if(val == PoisonPill.POIION_PILL)
                    {
                        System.out.println("Thread2 got POISION_PILL");
                        System.out.println("Thread2 exiting");
                        return;
                    }

                    System.out.println("Thread2 consumed:" + val);

                    Thread.sleep(2200);

                    if(val % 2 == 0)
                    {
                        mQueueOut.put(val);
                    }

                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    System.out.println("Thread2 interrupted");
                    System.out.println("Thread2 exiting");
                    return;
                }
            }
        }
    }
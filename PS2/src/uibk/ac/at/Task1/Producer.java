package uibk.ac.at.Task1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Producer extends Thread
{
    private Random mRand;
    private Buffer mBuffer;
    private String mName;

    private final ReentrantLock mLock = new ReentrantLock(true);
    private final Condition contHasElement = mLock.newCondition();

    private List<Consumer> mWaitingConsumers;

    public Producer(String name)
    {
        super();
        mRand = new Random();
        mBuffer = new Buffer();
        mName = name;
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            Integer val = generateRandom(10);
            System.out.println("["+mName+"] generated " +val);

            mLock.lock();
            mBuffer.push(val);
            contHasElement.signalAll();
            mLock.unlock();

            //wait up to 3 Seconds
            try {
                val = generateRandom(3000);
                System.out.println("["+mName+"] wait " + val/1000.0 + "sec");
                Thread.sleep(val);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private int generateRandom(int max)
    {
        return mRand.nextInt(max);
    }

    public Integer consume() throws InterruptedException
    {
        mLock.lock();
        try
        {
            while(mBuffer.isEmpty())
                contHasElement.await();

            Integer val = mBuffer.pop();
            contHasElement.signal();
            return val;
        }
        finally
        {
            mLock.unlock();
        }
    }
}

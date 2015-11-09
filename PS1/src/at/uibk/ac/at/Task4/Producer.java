package at.uibk.ac.at.Task4;

import java.util.Random;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Producer extends Thread
{
    private Random mRand;
    private Buffer mBuffer;

    public Producer()
    {
        super();
        mRand = new Random();
        mBuffer = new Buffer();
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            Integer val = generateRandom(100);
            System.out.println("[Producer] generated " +val);
            mBuffer.push(val);

            //wait up to 3 Seconds
            try {
                val = generateRandom(3000);
                System.out.println("[Producer] wait " + val/1000.0 + "sec");
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

    public Integer tryConsume()
    {
        if(mBuffer.isEmpty())
            return null;

        return  mBuffer.pop();
    }
}

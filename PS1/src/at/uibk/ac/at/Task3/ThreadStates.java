package at.uibk.ac.at.Task3;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class ThreadStates implements Runnable
{
    private Object lock = new Object();
    private Thread mMonitoringThread;
    private List<CustomThread> mThreads;

    public ThreadStates()
    {
        mThreads = new LinkedList<CustomThread>();
    }

    public void SpwanThread()
    {
        CustomThread thread = new CustomThread();
        synchronized (lock)
        {
            mThreads.add(thread);
        }
    }

    void startMonitoring()
    {
        mMonitoringThread = new Thread(this);
        mMonitoringThread.start();
    }

    public void stopMonitoring()
    {
        mMonitoringThread.interrupt();

        for(Thread thread : mThreads)
            thread.interrupt();

        for (Thread thread : mThreads)
        {
            try
            {
                thread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            mMonitoringThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            System.out.println();
            System.out.println("****************************************************************");
            System.out.println("Thread Info:");
            int index = 1;

            synchronized (lock)
            {
                for(Thread thread : mThreads)
                {
                    System.out.println("Thread "  + index++ + ":");
                    System.out.println("Name:\t" + thread.getName());
                    System.out.println("State:\t" + thread.getState());
                    System.out.println("Stack:\t" + Thread.getAllStackTraces().keySet());
                    System.out.println();
                }
                System.out.println("****************************************************************");


                boolean allFinished = true;
                for(CustomThread thread : mThreads)
                {
                    switch (thread.getState())
                    {
                        case NEW:
                        {
                            thread.start();
                            break;
                        }
                        case TIMED_WAITING:
                        {
                            thread.setCurrentState(eThreadState.Waiting);
                            break;
                        }
                        case WAITING:
                        {
                            thread.setCurrentState(eThreadState.Resume);
                            break;
                        }
                        case RUNNABLE:
                        {
                            if(thread.getCurrentState() == eThreadState.Resume)
                                thread.setCurrentState(eThreadState.Block);
                            else
                                thread.setCurrentState(eThreadState.Timed_Waiting);
                            break;
                        }
                        case BLOCKED:
                        {
                            thread.setCurrentState(eThreadState.Dead);
                        }
                    }

                    if (thread.getState() != Thread.State.TERMINATED)
                        allFinished = false;
                }


                if(allFinished && !mThreads.isEmpty())
                {
                    System.out.println("All threads terminated...");
                    System.out.println("Press any key to exit...");
                    return;
                }
            }


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    //Spawns 5 threads
    //Each thread steps through all Thread states
    //New->Runnable->Waiting->Timed_Waiting->Runnable(Resume)->Blocked->Terminated
    public static void main(String[] args) throws IOException, InterruptedException
    {
        ThreadStates mgr = new ThreadStates();

        mgr.startMonitoring();

        for(int i = 0; i < 5; i++)
        {
            mgr.SpwanThread();
            Thread.sleep(3100);
        }



        System.in.read();
    }
}

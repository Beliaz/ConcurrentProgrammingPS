package at.uibk.ac.at.Pi;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Florian on 07.11.2015.
 */
class ThreadPool
{
    private Queue<Runnable> pending = new LinkedList();
    private Thread[] threads;

    public ThreadPool(int size)
    {
        this.threads = new Thread[size];
    }

    public synchronized void  enqueue(Runnable runnable)
    {
        pending.add(runnable);

        for(int i = 0; i < threads.length; i++)
        {
            if(threads[i] == null) {
                startNextTask(i);
                return;
            }
        }
    }

    public synchronized void onTaskCompleted(int threadIdx)
    {
        startNextTask(threadIdx);
    }

    private void startNextTask(int threadIdx) {
        if(!pending.isEmpty()) {
            threads[threadIdx] = new Thread(new ThreadPoolRunnable(this, threadIdx, pending.remove()));
            threads[threadIdx].start();
        }
    }
}

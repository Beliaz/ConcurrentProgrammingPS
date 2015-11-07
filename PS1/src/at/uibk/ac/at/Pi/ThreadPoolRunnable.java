package at.uibk.ac.at.Pi;

/**
 * Created by Florian on 07.11.2015.
 */
class ThreadPoolRunnable implements Runnable
{
    private ThreadPool owner;
    private Runnable runnable;
    private int threadIdx;

    public ThreadPoolRunnable(ThreadPool owner, int threadIdx, Runnable runnable)
    {
        this.owner = owner;
        this.runnable = runnable;
        this.threadIdx = threadIdx;
    }


    @Override
    public void run() {
        runnable.run();
        owner.onTaskCompleted(threadIdx);
    }
}

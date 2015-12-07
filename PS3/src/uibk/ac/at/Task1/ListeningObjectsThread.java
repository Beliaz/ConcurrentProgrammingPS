package uibk.ac.at.Task1;

import java.util.List;

/**
 * Created by flori on 06.12.2015.
 */
public class ListeningObjectsThread extends Thread
{
    private List<EventGeneratorThread> generatorThreads;
    private volatile boolean stop;

    public ListeningObjectsThread(List<EventGeneratorThread> generatorThreads)
    {
        this.generatorThreads = generatorThreads;
    }

    @Override
    public void run()
    {
        final int sleepTime = 4000;
        while(!Thread.currentThread().isInterrupted())
        {
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                return;
            }

            generatorThreads.forEach(EventGeneratorThread::infoListeningObjects);
        }
    }
}

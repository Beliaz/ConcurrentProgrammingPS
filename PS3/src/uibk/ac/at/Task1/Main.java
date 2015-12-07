package uibk.ac.at.Task1;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final int generatorCount = 2;
        final int additionalGenerators = 4;
        final int executionTime = 10000;

        List<MySafeListener> listeners = new LinkedList<>();
        List<EventGeneratorThread> generatorThreads = new LinkedList<>();

        for(int i = 0; i < generatorCount; i++)
        {
            EventGeneratorThread generatorThread = new EventGeneratorThread();
            listeners.add(MySafeListener.newInstance(generatorThread));

            generatorThreads.add(generatorThread);
        }

        for(int i = 0; i < additionalGenerators; i++)
        {
            EventGeneratorThread generatorThread = new EventGeneratorThread();
            generatorThreads.add(generatorThread);

            if(i < additionalGenerators - 1)
            {
                for(MySafeListener listener : listeners)
                {
                    listener.register(generatorThread);
                }
            }
        }

        List<Thread> threads = new LinkedList<>();
        threads.addAll(generatorThreads);
        threads.add(new ListeningObjectsThread(generatorThreads));

        threads.forEach(Thread::start);

        try {
            Thread.sleep(executionTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threads.forEach(Thread::interrupt);

        for(Thread th : threads)
        {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

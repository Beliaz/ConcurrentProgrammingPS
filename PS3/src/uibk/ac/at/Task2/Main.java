package uibk.ac.at.Task2;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void testSafe()
    {
        final int threadCount = 4;
        final AtomicBoolean stop = new AtomicBoolean(false);
        final SafeNumberRange unsafeRange = new SafeNumberRange();
        final List<Thread> threads = new LinkedList<>();

        for(int i = 0; i < threadCount; i++)
        {
            final int idx = i;
            threads.add(new Thread(() ->
            {
                final Random random = new Random();
                while(!stop.get())
                {
                    try
                    {
                        unsafeRange.setLower(random.nextInt());
                        unsafeRange.setUpper(random.nextInt());
                    }
                    catch (IllegalArgumentException e)
                    {
                        // consume exception
                        // not important as we are just
                        // trying violate the invariant
                    }
                    catch (IllegalStateException e)
                    {
                        // invariant violated
                        e.printStackTrace();
                        stop.set(true);
                        return;
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }));
        }

        threads.add(new Thread(()->
        {
            while(!stop.get())
            {
                final Random random = new Random();
                final int i = random.nextInt();

                System.out.format("isInRange(%d) == %s\n", i, unsafeRange.isInRange(i) ? "true" : "false");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }));

        threads.forEach(Thread::start);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stop.set(true);

        for(Thread th : threads)
        {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testUnsafe()
    {
        final int threadCount = 4;
        final AtomicBoolean stop = new AtomicBoolean(false);
        final NumberRange unsafeRange = new NumberRange();
        final List<Thread> threads = new LinkedList<>();

        for(int i = 0; i < threadCount; i++)
        {
            final int idx = i;
            threads.add(new Thread(() ->
            {
                final Random random = new Random();
                while(!stop.get())
                {
                    try
                    {
                        unsafeRange.setLower(random.nextInt());
                        unsafeRange.setUpper(random.nextInt());
                    }
                    catch (IllegalArgumentException e)
                    {
                        // consume exception
                        // not important as we are just
                        // trying violate the invariant
                    }
                    catch (IllegalStateException e)
                    {
                        // invariant successfully violated
                        e.printStackTrace();
                        stop.set(true);
                        return;
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }));
        }

        threads.add(new Thread(()->
        {
            while(!stop.get())
            {
                final Random random = new Random();

                unsafeRange.isInRange(random.nextInt());

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }));

        threads.forEach(Thread::start);

        for(Thread th : threads)
        {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] params)
    {
        // testUnsafe();
        testSafe();
    }
}

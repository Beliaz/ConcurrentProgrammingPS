package uibk.ac.at.Task3;

public class Main {

    public static void main(String[] args)
    {
        TestLazyInit(new LazyInitRaceCondition());
        TestLazyInit(new LazyInitSynchronized());
        TestLazyInit(new LazyInitAtomic());
    }

    private static void TestLazyInit(final LazyInit init)
    {
        Thread[] threads = new Thread[3];

        System.out.println(init.getClass().getName());

        for(int i = 0; i < threads.length; i++)
        {
            final int idx = i;
            threads[i] = new Thread(()->
            {
                Object obj = init.getInstance();
                System.out.format("Thread[%d] hash: %d\n", idx, obj.hashCode());
            });
        }

        for(Thread th : threads)
        {
            th.start();
        }

        for(Thread th : threads)
        {
            try
            {
                th.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println();
    }
}

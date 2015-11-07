package at.uibk.ac.at.Pi;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Pi
{
    public static Future<BigDecimal> calc(int iterations, int threadCount)
    {
        if(threadCount > iterations)
            threadCount = iterations;

        ThreadPool pool = new ThreadPool(threadCount);
        Accumulator accumulator = new Accumulator();

        final int partitionSize = iterations / threadCount;

        int count = 0;
        for(int i = 0; i + partitionSize <= iterations; i += partitionSize)
        {
            pool.enqueue(new PiCalcRunnable(i, partitionSize, accumulator));
            count++;
        }

        final int remainder = iterations % threadCount;
        if(remainder > 0)
        {
            pool.enqueue(new PiCalcRunnable(iterations - remainder, remainder, accumulator));
            count++;
        }

        final int taskCount = threadCount + (remainder > 0 ? 1 : 0);

        return new Future<BigDecimal>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return accumulator.getAccumulations() == taskCount;
            }

            @Override
            public BigDecimal get() throws InterruptedException, ExecutionException
            {
                accumulator.waitForAccumulations(taskCount);
                return accumulator.getResult().multiply(new BigDecimal(4));
            }

            @Override
            public BigDecimal get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new UnsupportedOperationException();
            }
        };
    }
}

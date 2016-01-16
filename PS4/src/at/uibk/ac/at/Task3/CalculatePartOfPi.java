package at.uibk.ac.at.Task3;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Florian on 15.01.2016.
 */
public class CalculatePartOfPi implements RunnableFuture<BigDecimal> {

    private int start;
    private int num;
    private volatile boolean isDone;
    private BigDecimal sum;

    public CalculatePartOfPi(int start, int num)
    {
        this.start = start;
        this.num = num;
        this.sum = new BigDecimal(0);
        this.isDone = false;
    }

    @Override
    public void run() {
        for(int n = start; n < (start + num); n++)
        {
            int sign = (int)Math.pow(-1, n);
            double summand = sign * 1.0 / (2.0 * n + 1.0);

            sum = sum.add(new BigDecimal(summand));
        }

        isDone = true;
    }

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
        return isDone;
    }

    @Override
    public BigDecimal get() throws InterruptedException, ExecutionException {
        while(!isDone) { }
        return sum;
    }

    @Override
    public BigDecimal get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.nanoTime();
        while(!isDone)
        {
            long elapsed = System.nanoTime() - start;
            if(unit.convert(elapsed, TimeUnit.NANOSECONDS) > timeout) {
                throw new TimeoutException();
            }
        }

        return sum;
    }
}

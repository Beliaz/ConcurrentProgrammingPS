package at.uibk.ac.at.Task3;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class CalcPi
{
    private ExecutorService executorService;

    public void initialize() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void uninitialize() {
        executorService.shutdown();
    }

    public BigDecimal calc(int objects, int iterationsPerObject) throws ExecutionException, InterruptedException {

        List<Future<BigDecimal>> parts = new LinkedList<>();

        for(int i = 0; i < objects; i++)
        {
            CalculatePartOfPi task = new CalculatePartOfPi(i * iterationsPerObject, iterationsPerObject);
            executorService.execute(task);
            parts.add(task);
        }

        BigDecimal sum = new BigDecimal(0);
        for(Future<BigDecimal> part : parts)
        {
            sum = sum.add(part.get());
        }

        return sum.multiply(new BigDecimal(4));
    }

    public BigDecimal calcAndProfile(int objects, int iterationsPerObject) throws ExecutionException, InterruptedException {

        long start = System.nanoTime();

        List<Future<BigDecimal>> parts = new LinkedList<>();

        for(int i = 0; i < objects; i++)
        {
            CalculatePartOfPi task = new CalculatePartOfPi(i * iterationsPerObject, iterationsPerObject);
            executorService.execute(task);
            parts.add(task);
        }

        BigDecimal sum = new BigDecimal(0);
        for(Future<BigDecimal> part : parts)
        {
            sum = sum.add(part.get());
        }

        sum = sum.multiply(new BigDecimal(4));

        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        System.out.format("Time: %d ms\n", elapsed);

        return sum;
    }

    public BigDecimal calcAndProfileWithoutInstantiation(int objects, int iterationsPerObject) throws ExecutionException, InterruptedException {

        List<RunnableFuture<BigDecimal>> parts = new LinkedList<>();

        for(int i = 0; i < objects; i++)
        {
            parts.add(new CalculatePartOfPi(i * iterationsPerObject, iterationsPerObject));
        }

        long start = System.nanoTime();

        for(Runnable part : parts)
        {
            executorService.execute(part);
        }

        //parts.forEach(executorService::execute);

        BigDecimal sum = new BigDecimal(0);
        for(Future<BigDecimal> part : parts)
        {
            sum = sum.add(part.get());
        }

        sum = sum.multiply(new BigDecimal(4));

        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        System.out.format("Time w/o inst.: %d ms\n", elapsed);

        return sum;
    }
}

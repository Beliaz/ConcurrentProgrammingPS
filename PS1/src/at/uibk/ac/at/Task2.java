package at.uibk.ac.at;

import at.uibk.ac.at.Pi.Pi;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class Task2 {

    static final int iterationCount = 500000;
    static final int maxThreadCount = 20;

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        SortedMap<Double, Integer> times = new TreeMap<Double, Integer>();

        System.out.format("Iteration Count: %d\n\n", iterationCount);

        for(int threadCount = 1; threadCount <= maxThreadCount; threadCount++)
        {
            long start = System.nanoTime();

            BigDecimal pi = Pi.calc(iterationCount, threadCount).get();

            double elapsed = (System.nanoTime() - start) / 1000000.0;

            System.out.format("[%d Threads] PI: %.3f, Result: %.3f, Elapsed: %.2f ms\n", threadCount, Math.PI, pi, elapsed);

            times.put(elapsed, threadCount);
        }

        System.out.println();
        System.out.println("fastest: " + times.firstKey() + "(" + times.get(times.firstKey()) + " Threads)");
    }
}

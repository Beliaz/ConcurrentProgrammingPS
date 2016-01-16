package at.uibk.ac.at.Task2;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        final List<ComputationCenter> computationCenters = new LinkedList<>();

        computationCenters.add(new ComputationCenter(5, computationCenters));
        computationCenters.add(new ComputationCenter(10, computationCenters));
        computationCenters.add(new ComputationCenter(20, computationCenters));

        System.out.println();

        computationCenters.forEach(Thread::start);

        Thread jobGenerator = new JobGenerator(computationCenters);

        jobGenerator.start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jobGenerator.interrupt();
        computationCenters.forEach(Thread::interrupt);

        try {
            jobGenerator.join();

            for(Thread th : computationCenters)
            {
                th.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

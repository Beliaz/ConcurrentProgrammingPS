package at.uibk.ac.at.Task2;

import java.util.List;
import java.util.Random;

/**
 * Created by Florian on 15.01.2016.
 */
public class JobGenerator extends Thread {

    private List<ComputationCenter> computationCenters;

    public JobGenerator(List<ComputationCenter> computationCenters) {
        this.computationCenters = computationCenters;
    }

    @Override
    public void run() {
        Random rnd = new Random();

        while(!isInterrupted())
        {
            for(ComputationCenter c : computationCenters)
            {
                c.addJob(new Job(rnd.nextInt(5000) + 2000, rnd.nextInt(90) + 100));
            }

            try {
                Thread.sleep(rnd.nextInt(500) + 100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

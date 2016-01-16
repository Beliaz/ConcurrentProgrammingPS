package at.uibk.ac.at.Task2;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Florian on 15.01.2016.
 */
public class ComputationCenter extends Thread {

    private BlockingDeque<Job> jobs;
    private int power;
    private int moneyEarned;
    private int jobsExecuted;
    private List<ComputationCenter> centers;

    public ComputationCenter(int power, List<ComputationCenter> centers) {
        this.jobs = new LinkedBlockingDeque<>();
        this.power = power;
        this.moneyEarned = 0;
        this.jobsExecuted = 0;
        this.centers = centers;

        System.out.format("[%d] power: %d\n", getId(), power);
    }

    public void addJob(Job job) {
        jobs.addFirst(job);
    }

    @Override
    public void run() {
        while(!isInterrupted())
        {
            Job job = null;

            try
            {
                job = jobs.removeLast();
            }
            catch(NoSuchElementException ex)
            {
                job = stealJob();
            }

            if(job != null)
            {
                try
                {
                    executeJob(job);
                }
                catch (InterruptedException ex)
                {
                    break;
                }
            }
        }

        System.out.format("[%d] completed %d jobs and earned %d€\n", getId(), jobsExecuted, moneyEarned);
    }

    private Job stealJob()
    {
        // steal
        Job bestJob = null;
        int bestJobCenterIdx = 0;
        for(int i = 0; i < centers.size(); i++)
        {
            if(centers.get(i).getId() == getId()) continue;

            Job job = centers.get(i).steal();

            if(job == null) continue;

            if(bestJob == null)
            {
                bestJob = job;
                bestJobCenterIdx = i;
            }
            else
            {
                if(job.ratio() > bestJob.ratio())
                {
                    centers.get(bestJobCenterIdx).addJob(bestJob);
                    bestJob = job;
                    bestJobCenterIdx = i;
                }
                else
                {
                    centers.get(i).addJob(job);
                }
            }
        }

        if(bestJob != null)
        {
            System.out.format("[%d] stole job from %d\n", getId(), centers.get(bestJobCenterIdx).getId());
        }

        return bestJob;
    }

    private void executeJob(Job job) throws InterruptedException
    {
        job.execute(power);

        moneyEarned += job.prize();
        jobsExecuted++;

        System.out.format("[%d]: finished job\n", currentThread().getId());
    }

    private Job steal()
    {
        return jobs.pollLast();
    }
}

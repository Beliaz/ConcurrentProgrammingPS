package at.uibk.ac.at.Task2;

/**
 * Created by Florian on 15.01.2016.
 */
public class Job
{
    private int effort;
    private int prize;

    public Job(int effort, int prize) {
        this.effort = effort;
        this.prize = prize;
    }

    public void execute(int power) throws InterruptedException {
        int time = effort / power;
        Thread.sleep(time);
    }

    public float ratio() {
        return this.prize / (float)this.effort;
    }

    public int prize() {
        return prize;
    }
}

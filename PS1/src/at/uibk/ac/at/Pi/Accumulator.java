package at.uibk.ac.at.Pi;

import java.math.BigDecimal;

class Accumulator
{
    private BigDecimal result = new BigDecimal(0);
    private int accumulations = 0;

    public synchronized void accumulate(BigDecimal value)
    {
        result = result.add(value);
        accumulations++;

        notifyAll();
    }

    public synchronized BigDecimal getResult() { return result; }

    public int getAccumulations() { return accumulations; }

    public synchronized void waitForAccumulations(int accumulations)
    {
        while(this.accumulations != accumulations)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package at.uibk.ac.at.Pi;

import java.math.BigDecimal;

class PiCalcRunnable implements Runnable
{
    private int start;
    private int num;
    private Accumulator accumulator;

    public PiCalcRunnable(int start, int num, Accumulator accumulator)
    {
        this.start = start;
        this.num = num;
        this.accumulator = accumulator;
    }

    @Override
    public void run()
    {
        BigDecimal sum = new BigDecimal(0);
        for(int n = start; n < (start + num); n++)
        {
            int sign = (int)Math.pow(-1, n);
            double summand = sign * 1.0 / (2.0 * n + 1.0);
            sum = sum.add(new BigDecimal(summand));
        }

        accumulator.accumulate(sum);
    }
}

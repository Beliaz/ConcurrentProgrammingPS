package uibk.ac.at.Task2;

import java.util.concurrent.atomic.*;

public class SafeNumberRange
{
    // INVARIANT: lower <= upper
    private volatile Integer lower = 0;
    private volatile Integer upper = 0;

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        synchronized (this)
        {
            if (i > upper) {
                throw new IllegalArgumentException("can't set upper to " + i + " < lower");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lower = i;

            if(lower > upper) {
                throw new IllegalStateException("invariant does not hold (lower <= upper)");
            }
        }
    }

    public void setUpper(int i) {

        synchronized (this)
        {
            // Warning -- unsafe check-then-act
            if (i < lower) {
                throw new IllegalArgumentException("can't set upper to " + i + " < lower");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            upper = i;

            if(upper < lower) {
                throw new IllegalStateException("invariant does not hold (upper >= lower)");
            }
        }
    }

    public boolean isInRange(int i) {

        return (i >= lower && i <= upper);
    }
}


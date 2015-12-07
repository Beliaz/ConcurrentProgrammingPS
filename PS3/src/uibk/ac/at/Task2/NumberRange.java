package uibk.ac.at.Task2;

import java.util.concurrent.atomic.*;

public class NumberRange
{
    // INVARIANT: lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i) {
        // Warning -- unsafe check-then-act
        if (i > upper.get()) {
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lower.set(i);

        if(lower.get() > upper.get()) {
            throw new IllegalStateException("invariant does not hold");
        }
    }

    public void setUpper(int i) {
        // Warning -- unsafe check-then-act
        if (i < lower.get()) {
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        upper.set(i);

        if(upper.get() < lower.get()) {
            throw new IllegalStateException("invariant does not hold");
        }
    }

    public boolean isInRange(int i) {

        return (i >= lower.get() && i <= upper.get());
    }
}


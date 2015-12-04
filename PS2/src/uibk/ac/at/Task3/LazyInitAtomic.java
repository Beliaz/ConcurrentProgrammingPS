package uibk.ac.at.Task3;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by flori on 21.11.2015.
 */
public class LazyInitAtomic implements LazyInit
{
    private AtomicReference<ExpensiveObject> instance = new AtomicReference<>(null);

    @Override
    public ExpensiveObject getInstance()
    {
        instance.compareAndSet(null, new ExpensiveObject());
        return instance.get();
    }
}


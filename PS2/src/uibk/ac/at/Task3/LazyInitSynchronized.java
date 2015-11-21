package uibk.ac.at.Task3;

/**
 * Created by flori on 21.11.2015.
 */
public class LazyInitSynchronized implements LazyInit
{
    private ExpensiveObject instance = null;

    @Override
    public synchronized ExpensiveObject getInstance()
    {
        if (instance == null)
            instance = new ExpensiveObject();

        return instance;
    }
}

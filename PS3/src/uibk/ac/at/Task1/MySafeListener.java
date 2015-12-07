package uibk.ac.at.Task1;

/**
 * Created by flori on 06.12.2015.
 */
public class MySafeListener
{
    private final EventListener listener = e -> doSomething(e);

    void doSomething(Event e)
    {
        System.out.format("MySafeListener [%d] received: %s\n", hashCode(), e.toString());
    }

    public void register(EventSource source)
    {
        source.registerListener(listener);
    }

    public static MySafeListener newInstance(EventSource source){
        MySafeListener safe = new MySafeListener();
        source.registerListener(safe.listener);
        return safe;
    }
}

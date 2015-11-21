package uibk.ac.at.Task3;

/**
 * Created by flori on 21.11.2015.
 */
public class ExpensiveObject
{
    public ExpensiveObject()
    {
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

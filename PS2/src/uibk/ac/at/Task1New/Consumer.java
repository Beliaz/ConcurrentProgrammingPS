package uibk.ac.at.Task1New;

import java.util.List;

/**
 * Created by flori on 22.11.2015.
 */
public class Consumer implements Runnable {

    private String name;
    private Broker broker;

    public Consumer(String name)
    {
        this.name = name;
        broker = new Broker(this);
    }

    public Broker getBroker() { return broker; }

    public String getName() { return name; }

    @Override
    public void run()
    {
        // demand() blocks until all producers
        // have offered at least one item and returns true
        // or if there are no producers left
        // returns false
        while(broker.demand())
        {
            // retrieves numbers from the producers
            // and remove one if it supplies 0
            List<Integer> goods = broker.getGoods();

            // print out values
            for(Integer item : goods)
            {
                System.out.format("[Consumer] %s consumed %d\n", getName(), item);
            }
        }
    }
}

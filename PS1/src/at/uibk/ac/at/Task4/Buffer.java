package at.uibk.ac.at.Task4;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Mathias Hölzl on 07.11.2015.
 */
public class Buffer
{
    private Queue<Integer> mData;

    public Buffer()
    {
        mData = new LinkedList<Integer>();
    }

    public synchronized void push(int v)
    {
        mData.add(v);
    }

    public synchronized int pop()
    {
        return mData.remove();
    }

    public synchronized boolean isEmpty()
    {
        return mData.isEmpty();
    }
}

package uibk.ac.at.Task2;

import sun.java2d.SurfaceDataProxy;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;


// S.Ostermann

// a receive object 
// stores the message received by another process
// and is used for synchronization
// ------------------------------------------------------------------------
class Receive {
    public Receive() {
    }

    public void setMessage(String message) { this.message = message; }
    public String getMessage() { String ret = message; message = null; return ret; }

    // ------------------------------------------------------------------------
    private String message = null;
}


// ------------------------------------------------------------------------
class Process extends Thread{
// ------------------------------------------------------------------------

    int pid; // process id
    Process[] p; // references to all other processes
    Receive[] receive; // the receive objects
    CountDownLatch latch;

    Process(int pid, int n)
    {
        if(n <= 0) throw new IllegalArgumentException();

        this.pid = pid;
        if (this.pid==0) { // p0 creates the vector of references
            this.p = new Process[n];
            this.p[0]=this;
            this.latch = new CountDownLatch(n-1);
        }
        receive = new Receive[n];
        for (int i=0; i<n; i++)
            receive[i] = new Receive();
    }

    // remote, called by p0, at all others, to transfer the reference vector
    // --------------------------------------------------------------------
    public synchronized void neighbours(Process[] p) {
        // --------------------------------------------------------------------
        this.p = p;
        this.notify();
    }

    // remote, called by others, at p0, to send their reference to p0
    // --------------------------------------------------------------------
    public synchronized void register(Process p, int pid) {
        // --------------------------------------------------------------------
        this.p[pid] = p; // p0 stores references in the vector
        System.out.println("p" + pid +" registered");

        if(latch != null) latch.countDown();
    }

    // called by sender
    // --------------------------------------------------------------------
    public void sendMessage(String m, int pid)
    {
        synchronized (receive[pid])
        {
            receive[pid].setMessage(m);
            receive[pid].notify();
        }
    }
    // --------------------------------------------------------------------


    // called by a process to receive a message from another process
    // --------------------------------------------------------------------
    String receiveMessage(int i)
    {
        synchronized (receive[i])
        {
            String msg = receive[i].getMessage();
            while(msg == null)
            {
                try
                {
                    receive[i].wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                msg = receive[i].getMessage();
            }

            return msg;
        }
    }
    // --------------------------------------------------------------------


    // the activity of a process
    // --------------------------------------------------------------------
    public void run()
    {
        String m;

        if (pid==0) {

            try
            {
                latch.await();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            // p0 sends the reference vector to the others
            p[pid] = (Process)this;
            for (int i=1; i<p.length; i++) {
                try
                {
                    p[i].neighbours(p);
                }
                catch (Exception e)
                {
                    System.err.println("init exception:");
                    e.printStackTrace();
                }
            }
        }

        else {
            // wait until they got the reference vector
            synchronized (this)
            {
                if(p == null)
                {
                    try
                    {
                        this.wait();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        // send hello to every other process
        for (int i=0; i<p.length; i++) {
            if (i!=pid)  {
                try {
                    p[i].sendMessage("hello p" + i + ", this is p" + pid, pid);
                }
                catch (Exception e)
                {
                    System.err.println("send exception:");
                }
                System.out.format("p%d sent hello to p%d\n", pid, i);
            }
        }

        // receive message from every other one
        for (int i=p.length-1; i>=0; i--)
        {
            // for (int i=0; i<p.length; i++) {
            if (i!=pid)
            {
                System.out.format("p%d receiving from p%d\n", pid, i);
                m = receiveMessage(i);
                System.out.format("p%d received from p%d: %s\n", pid, i, m);
            }
        }

    }

    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);

        System.out.println("number of processes: " + n);

        Process one = new Process(0, n);
        one.start();


        for (int i = 1; i < n; i++)
        {
            Process temp = new Process(i, n);
            one.register(temp, i);
            temp.start();
        }
    }
}



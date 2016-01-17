package at.uibk.ac.at.Task1;

import at.uibk.ac.at.Task1.cancellation_threads.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("********************************************");
        System.out.println("Cancellation Example");
        System.out.println("press enter to send random data to thread 4 or write exit to cancel thread1");
        System.out.println("********************************************");
        System.out.println();

        BlockingQueue<Integer> queueAB = new LinkedBlockingDeque<Integer>();
        BlockingQueue<Integer> queueBC = new LinkedBlockingDeque<Integer>();

        int port = 1234;

        Thread1 a = new Thread1(queueAB);
        Thread2 b = new Thread2(queueAB, queueBC);
        Thread3 c = new Thread3(queueBC, port);
        Thread4 d = new Thread4(port);

        a.start();
        b.start();
        c.start();
        d.start();

        while (!c.isConnected())
            Thread.sleep(500);

        while (true)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = br.readLine();
            System.out.println();


            if (input != null && input.equals("exit"))
            {
                a.cancel();
                break;
            }


            char[] charArray = new char[100 + new Random().nextInt(10)*100];
            Arrays.fill(charArray, '_');
            String str = new String(charArray);

            System.out.println("Sending big data:" + charArray.length);

            c.write(str);
        }

        a.join();
        b.join();


        c.cancel();
        d.interrupt();

    }
}

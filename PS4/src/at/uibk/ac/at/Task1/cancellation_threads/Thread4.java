package at.uibk.ac.at.Task1.cancellation_threads;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Connects to a SocketServer at <strong>port</strong> and consumes data <br>
 * Cancellation via interrupt <br>
 * Interrupt is restored when run is called directly
 */
public class Thread4 extends Thread
    {
        Socket mSocket;
        DataInputStream mStream;
        int mPort;
        byte[] mBuffer;

        public Thread4(int port)
        {
            mPort = port;
            mBuffer = new byte[9000000];;
        }

        @Override
        public void run()
        {
            try
            {
                try {

                    mSocket = new Socket("localhost", mPort);
                    mStream = new DataInputStream(mSocket.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (!Thread.currentThread().isInterrupted())
                {

                    String data = null;

                    try {

                        if(mSocket.isConnected() && !mSocket.isInputShutdown())
                        {
                            try
                            {
                                data = mStream.readUTF();
                            }
                            catch (SocketTimeoutException e)
                            {
                                System.out.println("Read socket timeout");
                            }
                        }
                    }
                    catch (EOFException e)
                    {
                        if(mSocket.isClosed())
                            System.out.println("Thread4 disconnected with Thread3");
                    }
                    catch (IOException e)
                    {
                        if(mSocket.isClosed())
                            System.out.println("Thread4 disconnected with Thread3");
                        else
                            e.printStackTrace();
                    }


                    if(data != null)
                    {
                        System.out.println("Thread4 received: " + data);
                    }

                    try
                    {
                        Thread.currentThread().sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        //restore interrupt
                        System.out.println("Thread4 interrupted");
                        System.out.println("Thread4 exiting");
                        Thread.currentThread().interrupt();
                        return;
                    }

                }
            }
            finally
            {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Thread4 interrupted");
            System.out.println("Thread4 exiting");
        }
    }
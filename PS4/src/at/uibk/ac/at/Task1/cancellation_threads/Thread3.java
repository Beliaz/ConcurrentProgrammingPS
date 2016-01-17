package at.uibk.ac.at.Task1.cancellation_threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Consumes values from the <strong>in</strong> <br>
 * Sets up a ServerSocket at <strong>port</strong>,<br>
 * consumed values are written to the socket
 * Cancellation via {@link #cancel() cancel()} <br>
 */
public class Thread3 extends Thread
    {
        BlockingQueue<Integer> mQueue;
        ServerSocket mServer;
        Socket mSocket;
        DataOutputStream mStream;
        boolean mConnected;
        boolean mRunning;
        int mPort;

        /**
         * @exception IOException Thrown if unable to start server at given port
         * @param queue Inputqueue
         * @param port Server is listening on this port
         */
        public Thread3(BlockingQueue<Integer> queue, int port) throws IOException {
            mPort = port;
            mQueue = queue;
            mConnected = false;
            mRunning = true;

            mServer = new ServerSocket(mPort);
        }

        @Override
        public void run()
        {
            try {
                waitForConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try
            {
                mStream = new DataOutputStream(mSocket.getOutputStream());


                while (mRunning)
                {
                    try
                    {
                        //Forward to thread 4
                        Integer val = mQueue.poll(1, TimeUnit.SECONDS);

                        //timeout?
                        if(val == null)
                            continue;

                        System.out.println("Thread3 consumed:" + val);

                        write(val.toString());
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        System.out.println("Thread3 interrupted");
                        System.out.println("Thread3 exiting");
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Thread3 canceled");
                System.out.println("Thread3 exiting");


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if(mServer != null)
                        mServer.close();

                    if(mStream != null)
                        mStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Write data to the socket
         * @param data Data sent to the Stream
         */
        public synchronized void write(String data) throws Exception
        {
            if(!isConnected())
                throw new Exception("Not connected");

            mStream.writeUTF(data);
            mStream.flush();
        }

        /**
         * Provides information if a Client is connected
         * @return true if connected false if disconnected
         */
        public synchronized boolean isConnected() { return mConnected; }

        /**
         * Stop thread and close Server
         */
        public synchronized void cancel() { mRunning = false; }

        private void waitForConnection() throws IOException {
            mSocket = mServer.accept();
            System.out.println("Thread4 connected to Thread3");
            setConnected(true);
        }

        private synchronized void setConnected(boolean connected) { mConnected = connected; }

        private synchronized boolean running() { return mRunning; }


    }
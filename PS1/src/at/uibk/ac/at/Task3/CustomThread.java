package at.uibk.ac.at.Task3;

enum eThreadState
    {
        Waiting,
        Timed_Waiting,
        Runnable,
        Resume,
        Block,
        Sync_Block,
        Dead
    }

    public  class CustomThread extends Thread
    {
        private final Object lock = new Object();
        private volatile boolean mPaused = false;

        private eThreadState mState = eThreadState.Runnable;

        public CustomThread()
        {
            super();
            setName("CustomThread");
        }

        public synchronized eThreadState getCurrentState()
        {
            return mState;
        }

        public void setCurrentState(eThreadState state)
        {
            if(state == eThreadState.Resume)
            {
                synchronized (lock)
                {
                    lock.notifyAll();
                }
            }
            else if(state == eThreadState.Block)
            {
                final CustomThread that = this;
                Thread blockThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        synchronized (that)
                        {
                            while (that.getCurrentState() == eThreadState.Block)
                            {}
                        }
                    }
                });

                blockThread.start();
                while (blockThread.getState() != State.RUNNABLE){}

                mState = eThreadState.Sync_Block;
            }

            mState = state;
        }

        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted())
            {
                switch (getCurrentState())
                {
                    case Sync_Block:
                    {
                        synchronized (this)
                        {

                        }
                    }

                    case Runnable: break;
                    case Timed_Waiting:
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case Waiting:
                    {
                        synchronized (lock)
                        {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case Dead:
                    {
                        return;
                    }
                }
            }
        }
    }
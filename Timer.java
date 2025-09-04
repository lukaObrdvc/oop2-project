public class Timer
{
    private int seconds = 0;
    private volatile boolean running = false;
    private Thread timerThread;

    public void start()
    {
        if (running) return;
        running = true;

        timerThread = new Thread(() -> {
            try
            {
                while (running)
                {
                    Thread.sleep(1000); // waits 1 second
                    seconds++;
                }
            }
            catch (InterruptedException e)
            {
                
            }
        });
        
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void stop()
    {
        running = false;
        if (timerThread != null)
        {
            timerThread.interrupt();
        }
    }

    public void reset()
    {
        seconds = 0;
    }

    public int getSeconds()
    {
        return seconds;
    }
}

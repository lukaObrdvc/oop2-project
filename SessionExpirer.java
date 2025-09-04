public class SessionExpirer
{
    public static final SessionExpirer Instance = new SessionExpirer(60);
    
    private final Timer timer = new Timer();
    private final int sessionDuration; // in seconds
    private volatile boolean inDialog = false;

    public SessionExpirer(int sd)
    {
        if (sd < 5) sd = 5;
        
        sessionDuration = sd;
    }

    public void setInDialog(boolean v)
    {
        inDialog = v;
    }
    
    public void reset()
    {
        if (!inDialog)
        {
            timer.reset();
        }
    }

    public void forceReset()
    {
        timer.reset();
    }

    public int remaining()
    {
        return sessionDuration - timer.getSeconds();
    }

    public void start()
    {
        timer.start();
    }

    public void stop()
    {
        timer.stop();
        timer.reset();
    }
}

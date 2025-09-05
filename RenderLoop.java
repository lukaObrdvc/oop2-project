// @todo cleanup this stuff....

public class RenderLoop implements Runnable
{
    public static final int fps = 30;
    public static final long ms = (long)(1000.0f / fps);
    // public static final Thread renderingThread = new Thread(() -> {
    //         while (true)
    //         {
    //             try
    //             {
    //                 long start = System.currentTimeMillis();

    //                 AirplaneTrafficSimulator.Instance.repaintMap();

    //                 long elapsed = System.currentTimeMillis() - start;
                    
    //                 long sleep = ms - elapsed;
                    
    //                 if (sleep > 0) Thread.sleep(sleep);
    //             }
    //             catch (InterruptedException e)
    //             {
    //                 // break;
    //             }
    //         }
    // });

    // static
    // {
    //     renderingThread.setDaemon(true);
    //     renderingThread.start();
    // }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                long start = System.currentTimeMillis();

                // nothing else should call this
                AirplaneTrafficSimulator.Instance.repaintMap();

                long elapsed = System.currentTimeMillis() - start;
                    
                long sleep = ms - elapsed;
                    
                if (sleep > 0) Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                // break;
            }
        }
    }
}

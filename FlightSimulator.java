import java.util.*;
import java.lang.Math;

public class FlightSimulator
{
    public static final FlightSimulator Instance = new FlightSimulator();
    public static final int simulationTime = 200; // in milliseconds

    private volatile boolean simulationRunning = false;
    private List<FlyingContext> context = new ArrayList<>();

    private static final Thread simulationThread = new Thread(() -> {
            while (true)
            {
                // lastUpdateTime = System.currentTimeMillis();
                long startTime = System.currentTimeMillis();

                FlightSimulator.Instance.simulate();
                
                long endTime = System.currentTimeMillis();
                
                long sleepTime = simulationTime - (endTime - startTime);

                if (sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                    }
                    catch (InterruptedException e)
                    {
                        // to break?
                    }
                }
            }
    });

    private static final Thread initiatorThread = new Thread(() -> {
            while (true)
            {
                try
                {
                    Thread.sleep(1000); // 1s = 10min in simulation time
                    if (FlightSimulator.Instance.getSimulationRunning())
                    {
                        FlightSimulator.Instance.initiateFlying();
                    }
                }
                catch (InterruptedException e)
                {
                    
                }
            }
    });

    private static final Thread clockThread = new Thread(() -> {
            while (true)
            {
                try
                {
                    Thread.sleep(100);
                    if (FlightSimulator.Instance.getSimulationRunning())
                    {
                        AirplaneTrafficSimulator.Instance.tick();
                    }
                }
                catch (InterruptedException e)
                {
                    
                }
            }
    });

    static
    {
        simulationThread.setDaemon(true);
        simulationThread.start();

        initiatorThread.setDaemon(true);
        initiatorThread.start();

        clockThread.setDaemon(true);
        clockThread.start();
    }

    // context list
    // flightmanager
    // airportmanager
    private synchronized void simulate()
    {
        if (simulationRunning)
        {
            long currentTime = System.currentTimeMillis();
            
            for (int i = 0; i < FlightManager.Instance.numFlights(); i++)
            {
                FlyingContext ctx = context.get(i);
                
                if (!ctx.flying) continue;

                Flight f = FlightManager.Instance.getFlight(i);

                String startCode = f.getTakeoff();
                String endCode = f.getLanding();
                
                if (!AirportManager.Instance.hasCode(startCode)   ||
                    !AirportManager.Instance.hasCode(endCode)     ||
                     AirportManager.Instance.getHidden(startCode) ||
                     AirportManager.Instance.getHidden(endCode))
                {
                    ctx.flying = false;
                }

                if (currentTime >= ctx.endTime) ctx.flying = false;
            }
        }
    }

    // acc()
    // context list
    // flightmanager
    // @todo rename to init
    private synchronized void initiateFlying()
    {
        int acc = AirplaneTrafficSimulator.Instance.acc();
        int h = acc / 3600;
        int m = (acc % 3600) / 60;

        for (int i = 0; i < FlightManager.Instance.numFlights(); i++)
        {
            Flight f = FlightManager.Instance.getFlight(i);
            int fh = f.getHour();
            int fm = f.getMin();
            int dur = f.getDur();

            if (!(fh == h && (fm >= m && fm < m + 10))) continue;

            FlyingContext ctx = context.get(i);

            if (!ctx.flying)
            {
                ctx.startX = AirportManager.Instance.getAirport(f.getTakeoff()).getX();
                ctx.startY = AirportManager.Instance.getAirport(f.getTakeoff()).getY();
                ctx.endX   = AirportManager.Instance.getAirport(f.getLanding()).getX();
                ctx.endY   = AirportManager.Instance.getAirport(f.getLanding()).getY();
                
                long currentTime = System.currentTimeMillis();

                ctx.startTime = currentTime;
                ctx.endTime = currentTime + dur * 100; // 10 sim min = 1000 real ms  ->  1 sim min = 100 real ms
                
                ctx.flying = true;
            }
        }
    }

    public FlyingContext getContext(int i)
    {
        return context.get(i);
    }

    public void toggleFlying(int i)
    {
        FlyingContext ctx = context.get(i);
        
        if (ctx.flying) ctx.flying = false;
        else            ctx.flying = true;
    }

    public void addContext()
    {
        context.add(new FlyingContext());
    }

    public void setSimulationRunning(boolean v)
    {
        simulationRunning = v;
    }

    public void toggleSimulationRunning()
    {
        if (simulationRunning) simulationRunning = false;
        else                   simulationRunning = true;
    }
        
    public boolean getSimulationRunning()
    {
        return simulationRunning;
    }

    public void resetFlying()
    {
        for (FlyingContext ctx : context)
        {
            ctx.flying = false;
        }
    }
}

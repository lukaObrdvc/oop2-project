import java.awt.*;
import java.awt.event.*;

public class MapCanvas extends Canvas
{
    public static final int squareDim = 8;
    public static final int planeRadius = 8;
    public static final int margin = 2;
    public static final int blinkPeriod = 600; // in milliseconds
    private volatile static boolean blinkOn = false;
    private static final Thread blinkThread = new Thread(() -> {
            while (true)
            {
                try
                {
                    Thread.sleep(blinkPeriod);
                    if (blinkOn) blinkOn = false;
                    else blinkOn = true;
                }
                catch (InterruptedException e)
                {
                    // should this not break?
                }
            }
    });

    static
    {
        blinkThread.setDaemon(true);
        blinkThread.start();
    }

    private void drawAirports(Graphics g, int w, int h)
    {
        int half = squareDim / 2;
        
        for (Airport a : AirportManager.Instance.getAllAirports())
        {
            if (AirportManager.Instance.getHidden(a.getCode())) continue;
            
            if (AirportManager.Instance.getSelected(a.getCode()))
            {
                if (blinkOn) g.setColor(Color.RED);
                else         g.setColor(Color.GRAY);
            } else
            {
                g.setColor(Color.GRAY);
            }
            
            float x = a.getX();
            float y = a.getY();
            int px = toPixelX(x, w);
            int py = toPixelY(y, h);

            g.fillRect(px - half, py - half, squareDim, squareDim);

            g.setColor(Color.WHITE);

            // @todo fix this stuff...
            int codeX = px + half + 2;
            int codeY = py + half+ 2;

            if (codeX > w - margin)
            {
                codeX = px - half - 24;
            }
            if (codeY < margin)
            {
                codeY = py + squareDim + 12;
            }
            if (codeY > h - margin)
            {
                codeY = py - half - 2;
            }
            
            g.drawString(a.getCode(), codeX, codeY);
        }
    }

    private void drawPlanes(Graphics g, int w, int h)
    {
        for (int i = 0; i < FlightManager.Instance.numFlights(); i++)
        {
            FlyingContext ctx = FlightSimulator.Instance.getContext(i);
            
            if (!ctx.flying) continue;

            long currentTime = System.currentTimeMillis();

            float div = ctx.endTime - ctx.startTime;
            if (div <= 0) continue;

            float t = (float)(currentTime - ctx.startTime) / div;
            if (t < 0) t = 0;
            if (t > 1)
            {
                t = 1;
                FlightSimulator.Instance.toggleFlying(i);
            }

            int px = (int)(ctx.startX + t * (ctx.endX - ctx.startX));
            int py = (int)(ctx.startY + t * (ctx.endY - ctx.startY));
            
            g.setColor(Color.BLUE);
            g.fillOval(px, py, planeRadius, planeRadius);
        }
    }

    @Override
    public void update(Graphics g)
    {
        // prevents background clearing
        paint(g);
    }
    
    @Override
    public synchronized void paint(Graphics g)
    {
        // super.paint(g);
        Dimension size = getSize();

        int w = size.width;
        int h = size.height;

        Image backbuffer = createImage(w, h);
        Graphics bbg = backbuffer.getGraphics();
        
        bbg.setColor(Color.BLACK);
        bbg.fillRect(0, 0, w, h);

        drawAirports(bbg, w, h);
        drawPlanes(bbg, w, h);

        g.drawImage(backbuffer, 0, 0, this);
    }

    public void testCollision(int x, int y)
    {
        Dimension size = getSize();

        int w = size.width;
        int h = size.height;

        int half = squareDim / 2;
        
        for (Airport a : AirportManager.Instance.getAllAirports())
        {
            int px = toPixelX(a.getX(), w);
            int py = toPixelY(a.getY(), h);

            if (x >= px - half && x <= px + half &&
                y >= py - half && y <= py + half)
            {
                AirportManager.Instance.toggleSelected(a.getCode());
                break; // only one airport per click
            }
        }
    }

    private int toPixelX(float x, int w)
    {
        // [-90,90] → [0,w]
        // floors
        return (int) ((x + 90) / 180.0f * w);
    }

    private int toPixelY(float y, int h)
    {
        // [-90,90] → [h,0] so +90 is at top
        // floors
        return h - (int) ((y + 90) / 180.0f * h);
    }    
}

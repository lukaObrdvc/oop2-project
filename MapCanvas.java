import java.awt.*;
import java.awt.event.*;

public class MapCanvas extends Canvas
{
    public static final int squareDim = 8;
    public static final int margin = 2;
    public static final int blinkPeriod = 1000; // in milliseconds
    private volatile static boolean blinkOn = false;
    private static final Thread blinkThread = new Thread(() -> {
            while (true)
            {
                try
                {
                    Thread.sleep(blinkPeriod);
                    if (blinkOn) blinkOn = false;
                    else blinkOn = true;
                    // @speed if none is selected no need to repaint
                    AirplaneTrafficSimulator.Instance.repaintMap();
                }
                catch (InterruptedException e)
                {
                    
                }
            }
    });

    static
    {
        blinkThread.setDaemon(true);
        blinkThread.start();
    }
    
    @Override
    public void paint(Graphics g)
    {
        Dimension size = getSize();

        int w = size.width;
        int h = size.height;

        int half = squareDim / 2;
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

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

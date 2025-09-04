import java.awt.*;
import java.awt.event.*;

public class MapCanvas extends Canvas
{
    public static final int squareDim = 8;
    public static final int margin = 2;
    
    @Override
    public void paint(Graphics g)
    {
        Dimension size = getSize();

        int w = size.width;
        int h = size.height;
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);

        g.setColor(Color.GRAY);
        
        for (Airport a : AirportManager.Instance.getAllAirports())
        {
            if (AirportManager.Instance.getHidden(a.getCode())) continue;
            
            float x = a.getX();
            float y = a.getY();
            int px = toPixelX(x, w);
            int py = toPixelY(y, h);

            g.fillRect(px - squareDim / 2, py - squareDim / 2, squareDim, squareDim);

            g.setColor(Color.WHITE);

            int codeX = px + squareDim / 2 + 2;
            int codeY = py + squareDim / 2+ 2;

            if (codeX > w - margin)
            {
                codeX = px - squareDim / 2 - 24;
            }
            if (codeY < margin)
            {
                codeY = py + squareDim + 12;
            }
            if (codeY > h - margin)
            {
                codeY = py - squareDim / 2 - 2;
            }
            
            g.drawString(a.getCode(), codeX, codeY);
            g.setColor(Color.GRAY);
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

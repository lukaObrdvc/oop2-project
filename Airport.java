public class Airport
{
    private String name;
    private String code;
    private float x;
    private float y;

    public Airport(String n, String c, float xx, float yy) throws BadCoordinatesError, NotUniqueError, BadCodeError
    {
        if (xx < -90 || xx > 90) throw new BadCoordinatesError();
        if (yy < -90 || yy > 90) throw new BadCoordinatesError();
        if (c.length() != 3 || !(c.equals(c.toUpperCase()))) throw new BadCodeError();
        // @todo this error is too big for the form...
        if (AirportManager.Instance.hasCode(c)) throw new NotUniqueError();

        name = n;
        code = c;
        x = xx;
        y = yy;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public float getX()
    {
        return x;
    }
    
    public float getY()
    {
        return y;
    }    
}

public class BadCoordinatesError extends Exception
{
    public BadCoordinatesError()
    {
        super("X,Y should be between -90 and 90!");
    }
}

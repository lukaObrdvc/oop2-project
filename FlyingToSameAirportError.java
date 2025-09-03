public class FlyingToSameAirportError extends Exception
{
    public FlyingToSameAirportError()
    {
        super("Cannot fly from and to the same airport!");
    }
}

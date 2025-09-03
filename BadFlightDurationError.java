public class BadFlightDurationError extends Exception
{
    public BadFlightDurationError()
    {
        super("Duration cannot be negative!");
    }
}

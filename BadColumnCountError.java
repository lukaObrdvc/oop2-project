public class BadColumnCountError extends Exception
{
    public BadColumnCountError()
    {
        super("Column count in CSV should be 4 or 5!");
    }
}

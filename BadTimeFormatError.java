public class BadTimeFormatError extends Exception
{
    public BadTimeFormatError()
    {
        super("Hours, minutes are not in correct intervals!");
    }
}

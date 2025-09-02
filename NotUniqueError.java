public class NotUniqueError extends Exception
{
    public NotUniqueError()
    {
        super("Airport with this unique code already exists!");
    }
}

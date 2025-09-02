public class EmptyFieldError extends Exception
{
    public EmptyFieldError()
    {
        super("All fields must be filled!");
    }
}

public class EmptyFileError extends Exception
{
    public EmptyFileError()
    {
        super("CSV file is empty!");
    }
}

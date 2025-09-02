public class Flight
{
    private String takeoff;
    private String landing;
    private int h;
    private int m;
    // @todo dont use snake case :)
    private int flight_duration; // in minutes

    public Flight(String t, String l, int hh, int mm, int dur) throws BadTimeFormatError, BadCodeError, NotUniqueError
    {
        if (hh < 0 || hh > 23) throw new BadTimeFormatError();
        if (mm < 0 || mm > 59) throw new BadTimeFormatError();
        if (dur <= 0) throw new BadTimeFormatError(); // @todo maybe different error
        if (t.length() != 3 || !(t.equals(t.toUpperCase()))) throw new BadCodeError();
        if (l.length() != 3 || !(l.equals(l.toUpperCase()))) throw new BadCodeError();
        if (t.equals(l)) throw new NotUniqueError(); // @todo probably should use a different error

        // what if total time extends the current day
        // if t doesn't exist
        // if l doesn't exist

        takeoff = t;
        landing = l;
        h = hh;
        m = mm;
        flight_duration = dur;
    }

    public String getTakeoff()
    {
        return takeoff;
    }

    public String getLanding()
    {
        return landing;
    }

    public int getHour()
    {
        return h;
    }

    public int getMin()
    {
        return m;
    }

    public int getDur()
    {
        return flight_duration;
    }
}

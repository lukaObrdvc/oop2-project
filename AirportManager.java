import java.util.*;

public class AirportManager
{
    public static final AirportManager Instance = new AirportManager();
    
    private HashMap<String, Airport> airports = new HashMap<>();

    public void addAirport(Airport a)
    {
        airports.put(a.getCode(), a);
    }

    public Airport getAirport(String code)
    {
        return airports.get(code);
    }

    public Collection<Airport> getAllAirports()
    {
        return Collections.unmodifiableCollection(airports.values());
    }

    public boolean hasCode(String code)
    {
        return airports.containsKey(code);
    }
}

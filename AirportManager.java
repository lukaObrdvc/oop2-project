import java.util.*;

public class AirportManager
{
    public static final AirportManager Instance = new AirportManager();
    
    private HashMap<String, Airport> airports = new HashMap<>();
    private HashMap<String, Boolean> hidden = new HashMap<>();

    public void addAirport(Airport a)
    {
        airports.put(a.getCode(), a);
        hidden.put(a.getCode(), false);
        AirplaneTrafficSimulator.Instance.addAirport(a);
        AirplaneTrafficSimulator.Instance.repaintMap();
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

    public boolean getHidden(String code)
    {
        return hidden.get(code);
    }

    public void setHidden(String code, boolean v)
    {
        hidden.put(code, v);
        AirplaneTrafficSimulator.Instance.repaintMap();
    }

    public void toggleHidden(String code)
    {
        if (hidden.get(code)) hidden.put(code, false);
        else hidden.put(code, true);
        AirplaneTrafficSimulator.Instance.repaintMap();
    }
}

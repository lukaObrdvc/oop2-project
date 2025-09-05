import java.util.*;

public class FlightManager
{
    public static final FlightManager Instance = new FlightManager();
    
    private List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight f)
    {
        flights.add(f);
        AirplaneTrafficSimulator.Instance.addFlight(f);
        FlightSimulator.Instance.addContext();
    }

    public Collection<Flight> getAllFlights()
    {
        return Collections.unmodifiableList(flights);
    }

    public Flight getFlight(int i)
    {
        return flights.get(i);
    }

    public int numFlights()
    {
        return flights.size();
    }
}

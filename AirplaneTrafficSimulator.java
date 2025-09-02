import java.awt.*;
import java.awt.event.*;

// @todo static class instead of singletons for managers?
// @todo status label like running, finished, paused
// @todo improve messages for FlightDialog
// @todo oop hierarchy for dialogs???
// @todo make , character illegal for input...

// @todo error handling for CSV stuff...
// @todo perhaps do combined adding, refreshing and stuff, when adding flights/airports...??

public class AirplaneTrafficSimulator extends Frame
{
    public static final AirplaneTrafficSimulator Instance = new AirplaneTrafficSimulator();
    
    private Panel airportPanel;
    private Panel flightTable;
    
    public AirplaneTrafficSimulator()
    {
        super("Airplane Traffic Simulator");

        MenuBar menuBar = new MenuBar();

        Menu flightMenu = new Menu("New");
        MenuItem newAirport = new MenuItem("New Airport");
        MenuItem newFlight = new MenuItem("New Flight");
        newAirport.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    AirportDialog d = new AirportDialog(AirplaneTrafficSimulator.this);
                    d.setVisible(true);
                }
            });
        newFlight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    FlightDialog d = new FlightDialog(AirplaneTrafficSimulator.this);
                    d.setVisible(true);
                }
            });
        flightMenu.add(newAirport);
        flightMenu.add(newFlight);
        menuBar.add(flightMenu);

        Menu csvMenu = new Menu("CSV");
        MenuItem importCSV = new MenuItem("Import from CSV");
        MenuItem exportCSV = new MenuItem("Export to CSV");
        importCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ImportCSVDialog d = new ImportCSVDialog(AirplaneTrafficSimulator.this);
                    d.setVisible(true);
                }
            });        
        exportCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ExportCSVDialog d = new ExportCSVDialog(AirplaneTrafficSimulator.this);
                    d.setVisible(true);
                }
            });        
        csvMenu.add(importCSV);
        csvMenu.add(exportCSV);
        menuBar.add(csvMenu);

        setMenuBar(menuBar);

        setLayout(new BorderLayout());

        Panel controlPanel = new Panel(new FlowLayout());
        Label time = new Label("Time: 00:00");
        Button run = new Button("Run");
        Button pause = new Button("Pause");
        Button reset = new Button("Reset");
        controlPanel.add(time);
        controlPanel.add(run);
        controlPanel.add(pause);
        controlPanel.add(reset);
        add(controlPanel, BorderLayout.NORTH);

        Canvas map = new Canvas()
            {
                @Override
                public void paint(Graphics g)
                {
                    Dimension size = getSize();
                    g.setColor(Color.BLUE);
                    g.fillRect(0, 0, size.width, size.height);
                }
            };
        map.setBackground(Color.WHITE);
        map.setPreferredSize(null);
        add(map, BorderLayout.CENTER);

        Panel rightPanel = new Panel(new GridLayout(2, 1, 0, 0));

        Panel flightPanel = new Panel(new BorderLayout());
        
        Panel header = new Panel(new GridLayout(1, 5));
        header.add(new Label("Takeoff airport"));
        header.add(new Label("Landing airport"));
        header.add(new Label("Hour"));
        header.add(new Label("Minute"));
        header.add(new Label("Flight duration"));
        
        flightTable = new Panel(new GridLayout(0, 5));
        
        ScrollPane flightScrollablePanel = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        flightScrollablePanel.add(flightTable);
        flightScrollablePanel.setPreferredSize(new Dimension(300, 300));

        flightPanel.add(header, BorderLayout.NORTH);
        flightPanel.add(flightScrollablePanel, BorderLayout.CENTER);

        Panel flightPanelContainer = new Panel(new BorderLayout());
        
        flightPanelContainer.add(new Label("Flights", Label.CENTER), BorderLayout.NORTH);
        flightPanelContainer.add(flightPanel, BorderLayout.CENTER);
        
        airportPanel = new Panel(new GridLayout(0, 1, 0, 0));

        ScrollPane airportScrollablePanel = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        airportScrollablePanel.add(airportPanel);
        airportScrollablePanel.setPreferredSize(new Dimension(300, 300));

        Panel airportPanelContainer = new Panel(new BorderLayout());
        airportPanelContainer.add(new Label("Airports", Label.CENTER), BorderLayout.NORTH);
        airportPanelContainer.add(airportScrollablePanel, BorderLayout.CENTER);
        
        // validate and repaint when adding new items

        rightPanel.add(flightPanelContainer);
        rightPanel.add(airportPanelContainer);
        
        add(rightPanel, BorderLayout.EAST);

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        
        // @todo...?:
        // why is spacing between elements unresponsive
        // some padding on bottom still
        // can i make resizing less crappy for redrawing?
        // checkbox group or something...?

        // @todo padding, centering...?

        addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent we)
                {
                    // @todo maybe quit message first
                    
                    System.exit(0);
                }
            });
    }

    public void addFlight(Flight f)
    {
        flightTable.add(new Label(f.getTakeoff()));
        flightTable.add(new Label(f.getLanding()));
        flightTable.add(new Label(Integer.toString(f.getHour())));
        flightTable.add(new Label(Integer.toString(f.getMin())));
        flightTable.add(new Label(Integer.toString(f.getDur())));
        flightTable.validate();
    }

    public void addAirport(Airport a)
    {
        airportPanel.add(new Checkbox(a.getCode() + ": " + a.getName() + " (" + a.getX() + ", " + a.getY() + ")"));
        // @todo set to checked immediately
        airportPanel.validate();
    }

    public static void main(String[] args)
    {
        
    }
}


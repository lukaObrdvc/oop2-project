import java.awt.*;
import java.awt.event.*;

// @todo static class instead of singletons for managers?
// @todo status label like running, finished, paused
// @todo oop hierarchy for dialogs???
// @todo disable creation of more singletons for every singleton..?

// @todo more error handling for CSV stuff...
// @todo fix the form to include all error space and so on... (NotUniqueError)


// @todo...?:
// why is spacing between elements unresponsive
// some padding on bottom still
// can i make resizing less crappy for redrawing?
// checkbox group or something...?

// @todo padding, centering...?

// @todo disable a lot of stuff unless simulation is reseted??
// @todo maybe better code drawing placement?

// @todo auto deselect when going to hidden?

public class AirplaneTrafficSimulator extends Frame
{
    public static final AirplaneTrafficSimulator Instance = new AirplaneTrafficSimulator();
    
    private Panel airportPanel;
    private Panel flightTable;
    private MapCanvas map;
    private Label time;
    // private int acc = 0;
    private int acc = 7*60*60;
    
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
        time = new Label("00:00");
        Button run = new Button("Run");
        Button pause = new Button("Pause");
        Button reset = new Button("Reset");
        run.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    FlightSimulator.Instance.setSimulationRunning(true);
                }
            });
        pause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    FlightSimulator.Instance.setSimulationRunning(false);
                }
            });
        reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    AirplaneTrafficSimulator.Instance.resetTime();
                    FlightSimulator.Instance.setSimulationRunning(false);
                    FlightSimulator.Instance.resetFlying();
                }
            });

        controlPanel.add(time);
        controlPanel.add(run);
        controlPanel.add(pause);
        controlPanel.add(reset);
        add(controlPanel, BorderLayout.NORTH);

        map = new MapCanvas();
        map.setBackground(Color.WHITE);
        map.setPreferredSize(null);
        map.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e)
                {
                    map.testCollision(e.getX(), e.getY());
                }
            });
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
 
        rightPanel.add(flightPanelContainer);
        rightPanel.add(airportPanelContainer);
        
        add(rightPanel, BorderLayout.EAST);

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        
        addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent we)
                {
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
        Checkbox cb = new Checkbox(a.getCode() + ": " + a.getName() + " (" + a.getX() + "," + a.getY() + ")");
        cb.setState(true);
        cb.addItemListener(e -> {
                String code = cb.getLabel().substring(0, 3);
                AirportManager.Instance.toggleHidden(code);
            });
        
        airportPanel.add(cb);
        airportPanel.validate();
    }

    public void repaintMap()
    {
        map.repaint();
    }

    public synchronized void tick()
    {
        acc += 60;
        acc = acc % (86400); // seconds in 24h
        
        int h = acc / 3600;
        int m = (acc % 3600) / 60;

        time.setText(String.format("%02d:%02d", h, m));
        time.validate();
    }

    public void resetTime()
    {
        acc = 0;
        time.setText("00:00");
        time.validate();
    }

    public synchronized int acc()
    {
        return acc;
    }

    public static void main(String[] args)
    {
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {SessionExpirer.Instance.reset();},
                                                        AWTEvent.MOUSE_EVENT_MASK |
                                                        AWTEvent.KEY_EVENT_MASK   |
                                                        AWTEvent.WINDOW_EVENT_MASK);
        SessionExpirer.Instance.start();

        Thread rl = new Thread(new RenderLoop());

        rl.setDaemon(true);
        rl.start();
    }
}


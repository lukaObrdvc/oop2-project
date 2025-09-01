import java.awt.*;
import java.awt.event.*;

public class AirplaneTrafficSimulator extends Frame
{
    public AirplaneTrafficSimulator()
    {
        super("Airplane Traffic Simulator");

        MenuBar menuBar = new MenuBar();

        Menu flightMenu = new Menu("New");
        flightMenu.add(new MenuItem("New Airport"));
        flightMenu.add(new MenuItem("New Flight"));
        menuBar.add(flightMenu);

        Menu csvMenu = new Menu("CSV");
        csvMenu.add(new MenuItem("Import from CSV"));
        csvMenu.add(new MenuItem("Export to CSV"));
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
        
        Panel rows = new Panel(new GridLayout(0, 5));
        
        rows.add(new Label("Flight A"));
        rows.add(new Label("Flight B"));
        rows.add(new Label("Flight C"));
        rows.add(new Label("Flight D"));
        rows.add(new Label("Flight E"));
        
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        rows.add(new Label("Flight E"));
        
        rows.add(new Label("Flight E"));
        
        ScrollPane flightScrollablePanel = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        flightScrollablePanel.add(rows);
        flightScrollablePanel.setPreferredSize(new Dimension(300, 300));

        flightPanel.add(header, BorderLayout.NORTH);
        flightPanel.add(flightScrollablePanel, BorderLayout.CENTER);

        Panel flightPanelContainer = new Panel(new BorderLayout());
        
        flightPanelContainer.add(new Label("Flights", Label.CENTER), BorderLayout.NORTH);
        flightPanelContainer.add(flightPanel, BorderLayout.CENTER);
        
        Panel airportPanel = new Panel(new GridLayout(0, 1, 0, 0));
        airportPanel.add(new Checkbox("Airport A"));
        airportPanel.add(new Checkbox("Airport B"));
        airportPanel.add(new Checkbox("Airport C"));
        airportPanel.add(new Checkbox("Airport D"));
        airportPanel.add(new Checkbox("Airport E"));
        airportPanel.add(new Checkbox("Airport E"));
        airportPanel.add(new Checkbox("Airport E"));
        airportPanel.add(new Checkbox("Airport E"));
        airportPanel.add(new Checkbox("Airport E"));
        airportPanel.add(new Checkbox("Airport E"));

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

    public static void main(String[] args)
    {
        new AirplaneTrafficSimulator();
    }
}


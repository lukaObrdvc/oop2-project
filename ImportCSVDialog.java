import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ImportCSVDialog extends Dialog
{
    TextField filepath;
    Label errorLabel;
    
    public ImportCSVDialog(Frame parent)
    {
        super(parent, "Import from CSV", true); // true=modal

        setSize(320, 110);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        errorLabel = new Label("", Label.CENTER);
        errorLabel.setForeground(Color.RED);

        Panel formPanel = new Panel(new GridLayout(1, 2, 0, 0));
        filepath = new TextField();
        Label filepathLabel = new Label("Enter file path relative to cwd: ");
        formPanel.add(filepathLabel);
        formPanel.add(filepath);
        
        Panel buttonPanel = new Panel(new FlowLayout());
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        add(errorLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent we)
                {
                    dispose();
                }
            });
    }

    private void onOk()
    {
        int lineNum = 0;
        
        try
        {
            String fp = filepath.getText().trim();
            
            if (fp.isEmpty()) throw new EmptyFieldError();

            java.util.List<Airport> airports = new java.util.ArrayList<>();
            java.util.List<Flight> flights = new java.util.ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new FileReader(fp)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    lineNum++;
                    
                    String[] cols = line.split(",");

                    if (cols.length == 4) // airport
                    {
                        String name = cols[0].trim();
                        String code = cols[1].trim();
                        float x = Float.parseFloat(cols[2].trim());
                        float y = Float.parseFloat(cols[3].trim());
                        
                        Airport a = new Airport(name, code, x, y);
                        airports.add(a);
                    }
                    else if (cols.length == 5) // flight
                    {
                        String takeoff = cols[0].trim();
                        String landing = cols[1].trim();
                        int h = Integer.parseInt(cols[2].trim());
                        int m = Integer.parseInt(cols[3].trim());
                        int dur = Integer.parseInt(cols[4].trim());
                        
                        Flight f = new Flight(takeoff, landing, h, m, dur);
                        flights.add(f);
                    }
                    else
                    {
                        throw new BadColumnCountError();
                    }
                }
                
                if (lineNum == 0) throw new EmptyFileError();
            }
            
            for (Flight f : flights)
            {
                FlightManager.Instance.addFlight(f);
            }
            for (Airport a : airports)
            {
                AirportManager.Instance.addAirport(a);
            }
            
            dispose();
        }
        catch (IOException e)
        {
            errorLabel.setText(e.getMessage());
        }
        catch (NumberFormatException e)
        {
            errorLabel.setText("Line " + Integer.toString(lineNum) + ": " + "Coordinates must be valid numbers");
        }
        catch (Exception e)
        {
            if (lineNum != 0)
            {
                errorLabel.setText("Line " + Integer.toString(lineNum) + ": " + e.getMessage());
            }
            else
            {
                errorLabel.setText(e.getMessage());
            }
        }
    }
}

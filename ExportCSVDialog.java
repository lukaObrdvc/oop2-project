import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ExportCSVDialog extends Dialog
{
    TextField filepath;
    Label errorLabel;
    
    public ExportCSVDialog(Frame parent)
    {
        super(parent, "Export to CSV", true); // true=modal

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
        try
        {
            String fp = filepath.getText().trim();
            
            if (fp.isEmpty()) throw new EmptyFieldError();

            try (PrintWriter writer = new PrintWriter(fp))
            {
                Collection<Airport> airports = AirportManager.Instance.getAllAirports();
                Collection<Flight> flights = FlightManager.Instance.getAllFlights();

                for (Airport a : airports)
                {
                    String line = a.getName() + "," + a.getCode() + "," + Float.toString(a.getX()) + "," + Float.toString(a.getY());
                    writer.println(line);
                }

                for (Flight f : flights)
                {
                    String line = f.getTakeoff() + "," + f.getLanding() + "," + Integer.toString(f.getHour()) + "," + Integer.toString(f.getMin()) + "," + Integer.toString(f.getDur());
                    writer.println(line);
                }
            }

            dispose();
        }
        catch (IOException ioe)
        {
            errorLabel.setText("Error writing file: " + ioe.getMessage());
        }
        catch (Exception e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
}

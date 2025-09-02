import java.awt.*;
import java.awt.event.*;

public class FlightDialog extends Dialog
{
    TextField apcode_takeoff; // @todo dont use snake case :)
    TextField apcode_landing;
    TextField h;
    TextField m;
    TextField flight_duration;
    Label errorLabel;
    
    public FlightDialog(Frame parent)
    {
        super(parent, "New flight", true); // true=modal

        // setSize(200, 230);
        setSize(500, 500);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        errorLabel = new Label("", Label.CENTER);
        errorLabel.setForeground(Color.RED);

        Panel formPanel = new Panel(new GridLayout(5, 2, 6, 6));
        apcode_takeoff = new TextField();
        apcode_landing = new TextField();
        h = new TextField();
        m = new TextField();
        flight_duration = new TextField();
        Label takeoffLabel = new Label("Takeoff airport: ");
        Label landingLabel = new Label("Landing airport: ");
        Label hLabel = new Label("At hour: ");
        Label mLabel = new Label("At minute: ");
        Label durLabel = new Label("Duration in minutes: ");
        formPanel.add(takeoffLabel);
        formPanel.add(apcode_takeoff);
        formPanel.add(landingLabel);
        formPanel.add(apcode_landing);
        formPanel.add(hLabel);
        formPanel.add(h);
        formPanel.add(mLabel);
        formPanel.add(m);
        formPanel.add(durLabel);
        formPanel.add(flight_duration);

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
            String takeoff = apcode_takeoff.getText().trim();
            String landing = apcode_landing.getText().trim();
            String sh = h.getText().trim();
            String sm = m.getText().trim();
            String sdur = flight_duration.getText().trim();
            
            if (takeoff.isEmpty() || landing.isEmpty() || sh.isEmpty() || sm.isEmpty() || sdur.isEmpty()) throw new EmptyFieldError();

            int hh = Integer.parseInt(sh);
            int mm = Integer.parseInt(sm);
            int dur = Integer.parseInt(sdur);
            
            Flight f = new Flight(takeoff, landing, hh, mm, dur);
            FlightManager.Instance.addFlight(f);
            dispose();
        }
        catch (NumberFormatException e)
        {
            errorLabel.setText("Hours, minutes, duration must be valid numbers!");
        }
        catch (Exception e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
}

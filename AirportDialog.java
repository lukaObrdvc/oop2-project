import java.awt.*;
import java.awt.event.*;

public class AirportDialog extends Dialog
{
    TextField name;
    TextField code;
    TextField x;
    TextField y;
    Label errorLabel;
    
    public AirportDialog(Frame parent)
    {
        super(parent, "New airport", true); // true=modal

        setSize(200, 200);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        errorLabel = new Label("", Label.CENTER);
        errorLabel.setForeground(Color.RED);

        // this part here can be specialized, and else inherited...
        Panel formPanel = new Panel(new GridLayout(4, 2, 6, 6));
        name = new TextField();
        code = new TextField();
        x = new TextField();
        y = new TextField();
        Label nameLabel = new Label("Name: ");
        Label codeLabel = new Label("Code: ");
        Label xLabel = new Label("X: ");
        Label yLabel = new Label("Y: ");
        formPanel.add(nameLabel);
        formPanel.add(name);
        formPanel.add(codeLabel);
        formPanel.add(code);
        formPanel.add(xLabel);
        formPanel.add(x);
        formPanel.add(yLabel);
        formPanel.add(y);

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
            String n = name.getText().trim();
            String c = code.getText().trim();
            String sx = x.getText().trim();
            String sy = y.getText().trim();
            
            if (c.isEmpty() || n.isEmpty() || sx.isEmpty() || sy.isEmpty()) throw new EmptyFieldError();

            float xx = Float.parseFloat(sx);
            float yy = Float.parseFloat(sy);
            
            Airport a = new Airport(n, c, xx, yy);
            AirportManager.Instance.addAirport(a);
            dispose();
        }
        catch (NumberFormatException e)
        {
            errorLabel.setText("Coordinates must be valid numbers");
        }
        catch (Exception e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
}

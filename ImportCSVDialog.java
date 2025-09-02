import java.awt.*;
import java.awt.event.*;

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
        try
        {
            String fp = filepath.getText().trim();
            
            if (fp.isEmpty()) throw new EmptyFieldError();

            // find file
            // read line by line and store in some temporary buffer
            // make sure its proper csv format
            // make sure there are enough columns
            // if line fails basic input form for flight or airport then abort
            // otherwise read all that into temporary buffer(s) and then add to managers

            dispose();
        }
        catch (Exception e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
}

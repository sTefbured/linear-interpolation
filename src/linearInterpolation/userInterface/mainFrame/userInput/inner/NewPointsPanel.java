package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.event.ObjectUpdateEvent;
import linearInterpolation.model.listener.ObjectUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;

import javax.swing.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class NewPointsPanel extends JPanel implements ObjectUpdateListener {
    private final String tooltipText = "You must input initial values first.";
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private JFormattedTextField xField;
    private JButton addButton;

    public NewPointsPanel() {
        MainFrame.getInterpolation().addObjectUpdateListener(this);
        xField = createXField();
        addButton = createAddButton();
        setEnabledComponents(false);
        add(xField);
        add(addButton);
        setBorder(BorderFactory.createTitledBorder("Enter X"));
    }

    private JFormattedTextField createXField() {
        JFormattedTextField field = new JFormattedTextField(numberFormat);
        field.setColumns(5);
        return field;
    }

    private JButton createAddButton() {
        JButton button = new JButton("Add");
        button.addActionListener(e -> {
            try {
                MainFrame.getInterpolation().addPoint(parseField(xField));
            } catch (ParseException parseException) {
                //TODO: maybe insert number format error dialog
                parseException.printStackTrace();
            }
        });
        return button;
    }

    // TODO: maybe change name
    private void setEnabledComponents(boolean enabled) {
        xField.setEditable(enabled);
        xField.setToolTipText(enabled ? "" : tooltipText);
        addButton.setEnabled(enabled);
    }

    // TODO: move to utils class
    private Double parseField(JTextField field) throws ParseException {
        String text = field.getText();
        return numberFormat.parse(text).doubleValue();
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        // TODO: update listBox
        if (MainFrame.getInterpolation().isInitialized()) {
            setEnabledComponents(true);
        }
    }
}

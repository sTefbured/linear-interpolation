package linearInterpolation.userInterface.mainFrame.userInput;

import linearInterpolation.model.Interpolation;
import linearInterpolation.userInterface.mainFrame.userInput.inner.ValuesPanel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class InputPanel extends JPanel {
    public final int defaultValuesCount = 5;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final NumberFormat intFormat = NumberFormat.getIntegerInstance();

    private Interpolation interpolation;
    private final JPanel initializingPanel;
    private ValuesPanel valuesPanel;
    private JFormattedTextField valuesCountField;

    public InputPanel(Interpolation interpolation) {
        this.interpolation = interpolation;
        setLayout(new GridLayout(1, 2));
        initializingPanel = new JPanel();
        initializingPanel.setLayout(new BoxLayout(initializingPanel,
                BoxLayout.Y_AXIS));
        initializingPanel.setBorder(BorderFactory
                .createTitledBorder("Initial values"));
        initializingPanel.add(createCountPanel());
        valuesPanel = new ValuesPanel(defaultValuesCount, interpolation);
        initializingPanel.add(valuesPanel);
        add(initializingPanel);
        add(createNewPointsPanel());
    }

    private JPanel createCountPanel() {
        JPanel borderPanel = new JPanel(new BorderLayout());
        JPanel countPanel = new JPanel(new FlowLayout());
        JButton setButton = new JButton("Set values count");
        valuesCountField = new JFormattedTextField(intFormat);

        valuesCountField.setValue(5);
        valuesCountField.setColumns(5);
        setButton.addActionListener(e -> updateValuesPanel());
        countPanel.add(valuesCountField);
        countPanel.add(setButton);
        borderPanel.add(countPanel, BorderLayout.WEST);
        return borderPanel;
    }

    // TODO: add listBox
    private JPanel createNewPointsPanel() {
        JPanel newPointsPanel = new JPanel();
        JFormattedTextField xField = new JFormattedTextField(numberFormat);
        xField.setColumns(5);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                interpolation.addPoint(parseField(xField));
            } catch (ParseException parseException) {
                showNumberFormatErrorDialog();
            }
        });
        newPointsPanel.setBorder(BorderFactory.createTitledBorder("Enter X"));
        newPointsPanel.add(xField);
        newPointsPanel.add(addButton);
        return newPointsPanel;
    }

    private void updateValuesPanel() {
        int count;
        try {
            count = intFormat.parse(valuesCountField.getText()).intValue();
        } catch (ParseException e) {
            showNumberFormatErrorDialog();
            return;
        }
        initializingPanel.remove(valuesPanel);
        valuesPanel = new ValuesPanel(count, interpolation);
        initializingPanel.add(valuesPanel);
        valuesPanel.revalidate();
    }

    private Double parseField(JTextField field) throws ParseException {
        String text = field.getText();
        return numberFormat.parse(text).doubleValue();
    }

    private void showNumberFormatErrorDialog() {
        JOptionPane.showMessageDialog(
                this,
                "Error. Wrong number format.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
        valuesPanel.setInterpolation(interpolation);
    }
}

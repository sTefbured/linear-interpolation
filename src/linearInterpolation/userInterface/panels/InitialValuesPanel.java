package linearInterpolation.userInterface.panels;

import linearInterpolation.model.Interpolation;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import static javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;

public class InitialValuesPanel extends JPanel {
    public final int defaultValuesCount = 5;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final NumberFormat intFormat = NumberFormat.getIntegerInstance();

    private ArrayList<JTextField> xValuesFields;
    private ArrayList<JTextField> yValuesFields;

    private Interpolation interpolation;
    private JPanel initializingPanel;
    private JPanel valuesPanel;
    private JFormattedTextField valuesCountField;

    public InitialValuesPanel(Interpolation interpolation) {
        this.interpolation = interpolation;
        setBorder(BorderFactory.createTitledBorder("Input values"));
        setLayout(new GridLayout(1, 2));
        initializingPanel = new JPanel();
        initializingPanel.setLayout(new BoxLayout(initializingPanel,
                BoxLayout.Y_AXIS));
        initializingPanel.add(createCountPanel());
        valuesPanel = createValuesPanel(defaultValuesCount);
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

    private JPanel createValuesPanel(int valuesCount) {
        JPanel borderPanel = new JPanel(new BorderLayout());
        JPanel valuesPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridLayout(valuesCount + 1, 2));
        JScrollPane scrollPane = new JScrollPane(fieldsPanel,
                VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 300));

        JPanel xLabelPanel = new JPanel();
        JPanel yLabelPanel = new JPanel();
        xLabelPanel.add(new JLabel("Time"));
        yLabelPanel.add(new JLabel("Temperature"));
        fieldsPanel.add(xLabelPanel);
        fieldsPanel.add(yLabelPanel);
        xValuesFields = new ArrayList<>(valuesCount);
        yValuesFields = new ArrayList<>(valuesCount);

        for (int i = 0; i < valuesCount; i++) {
            JFormattedTextField xField = new JFormattedTextField(numberFormat);
            JFormattedTextField yField = new JFormattedTextField(numberFormat);
            xField.setColumns(5);
            yField.setColumns(5);
            JPanel xPanel = new JPanel();
            JPanel yPanel = new JPanel();
            xPanel.add(xField);
            yPanel.add(yField);
            xValuesFields.add(xField);
            yValuesFields.add(yField);
            fieldsPanel.add(xPanel);
            fieldsPanel.add(yPanel);
        }
        JButton initializeButton = new JButton("Initialize");
        initializeButton.addActionListener(e -> initializeInterpolation());
        valuesPanel.add(scrollPane, BorderLayout.CENTER);
        valuesPanel.add(initializeButton, BorderLayout.SOUTH);
        borderPanel.add(valuesPanel, BorderLayout.WEST);
        return borderPanel;
    }

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
        valuesPanel = createValuesPanel(count);
        initializingPanel.add(valuesPanel);
        valuesPanel.revalidate();
    }

    // TODO: finish the method (calculation button)
    private void initializeInterpolation() {
        try {
            Collection<Double> timeStamps = parseFields(xValuesFields);
            Collection<Double> temperatures = parseFields(yValuesFields);
            interpolation.initialize(timeStamps, temperatures);
            getParent().repaint();
        } catch (ParseException e) {
            showNumberFormatErrorDialog();
        }
    }

    private Collection<Double> parseFields(Collection<JTextField> fields)
            throws ParseException {
        Collection<Double> values = new ArrayList<>(fields.size());
        for (JTextField f : fields) {
            values.add(parseField(f));
        }
        return values;
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
    }
}

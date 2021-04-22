package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.Interpolation;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

// TODO: initialize interpolation
public class ValuesPanel extends JPanel {
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final NumberFormat intFormat = NumberFormat.getIntegerInstance();

    private Interpolation interpolation;
    private ArrayList<JTextField> xValuesFields;
    private ArrayList<JTextField> yValuesFields;

    public ValuesPanel(int valuesCount, Interpolation interpolation) {
        setInterpolation(interpolation);
        setLayout(new BorderLayout());
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
        add(valuesPanel, BorderLayout.WEST);
    }

    public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

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
}

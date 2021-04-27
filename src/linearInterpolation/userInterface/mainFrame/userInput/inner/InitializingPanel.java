package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.event.ObjectUpdateEvent;
import linearInterpolation.model.listener.ObjectUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class InitializingPanel extends JPanel implements ObjectUpdateListener {
    public final int MAX_VALUES_COUNT = 125;
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final NumberFormat intFormat = NumberFormat.getIntegerInstance();
    public final int defaultValuesCount = 5;

    private ArrayList<JTextField> xValuesFields;
    private ArrayList<JTextField> yValuesFields;
    private JFormattedTextField valuesCountField;
    private JPanel valuesPanel;

    public InitializingPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Initial values"));
        add(createCountPanel());
        valuesPanel = createValuesPanel(defaultValuesCount);
        add(valuesPanel);
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        Collection<Double> xValues = MainFrame.getInterpolation().getXValues();
        Collection<Double> yValues = MainFrame.getInterpolation().getYValues();
        updateValuesPanel(xValues.size());
        fillValueFields(xValues, yValues);
    }

    private void fillValueFields(Collection<Double> xValues,
                                 Collection<Double> yValues) {
        Iterator<Double> xIterator = xValues.iterator();
        Iterator<Double> yIterator = yValues.iterator();
        for (int i = 0; i < xValuesFields.size(); i++) {
            xValuesFields.get(i).setText(xIterator.next().toString());
            yValuesFields.get(i).setText(yIterator.next().toString());
        }
    }

    private JPanel createCountPanel() {
        JPanel countPanel = new JPanel(new FlowLayout());
        valuesCountField = new JFormattedTextField(intFormat);
        JButton setButton = new JButton("Set values count");

        valuesCountField.setValue(defaultValuesCount);
        valuesCountField.setColumns(5);
        setButton.addActionListener(e -> updateValuesPanel(getValuesCount()));
        countPanel.add(valuesCountField);
        countPanel.add(setButton);
        return countPanel;
    }

    private int getValuesCount() {
        int count;
        try {
            count = intFormat.parse(valuesCountField.getText()).intValue();
            if (count <= 0 || count > MAX_VALUES_COUNT) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException | ParseException e) {
            showNumberFormatErrorDialog();
            return -1;
        }
        return count;
    }

    private void updateValuesPanel(int count) {
        remove(valuesPanel);
        valuesPanel = createValuesPanel(count);
        add(valuesPanel);
        valuesPanel.revalidate();
    }

    private JPanel createValuesPanel(int valuesCount) {
        JPanel valuesPanel = new JPanel();
        valuesPanel.setLayout(new BorderLayout());
        JPanel fieldsPanel = new JPanel(new GridLayout(valuesCount + 1, 2));
        JPanel outerFieldsPanel = new JPanel(new BorderLayout());
        outerFieldsPanel.add(fieldsPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(outerFieldsPanel,
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
        return valuesPanel;
    }

    private void initializeInterpolation() {
        try {
            Collection<Double> timeStamps = parseFields(xValuesFields);
            Collection<Double> temperatures = parseFields(yValuesFields);
            MainFrame.getInterpolation().initialize(timeStamps, temperatures);
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

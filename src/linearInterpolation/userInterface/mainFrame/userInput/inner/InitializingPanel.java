package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.event.InterpolationUpdateEvent;
import linearInterpolation.model.listener.InterpolationUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;
import linearInterpolation.userInterface.mainFrame.userInput.Parser;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class InitializingPanel extends JPanel implements InterpolationUpdateListener {
    public final int MAX_VALUES_COUNT = 125;
    public final int defaultValuesCount = 5;

    private final NumberFormat numberFormat;
    private final NumberFormat intFormat = NumberFormat.getIntegerInstance();

    private JFormattedTextField valuesCountField;
    private ArrayList<JTextField> timeValuesFields;
    private ArrayList<JTextField> temperatureValuesFields;
    private JPanel valuesPanel;

    public InitializingPanel() {
        numberFormat = new DecimalFormat() {
            @Override
            public Number parse(String source, ParsePosition parsePosition) {
                Number number = super.parse(source, parsePosition);
                if (number == null || number.doubleValue() < 0) {
                    parsePosition.setIndex(source.length());
                    parsePosition.setErrorIndex(-1);
                    number = null;
                }
                return number;
            }
        };
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Initial values"));
        add(createCountPanel());
        valuesPanel = createValuesPanel(defaultValuesCount);
        add(valuesPanel);
    }

    @Override
    public void interpolationUpdated(InterpolationUpdateEvent event) {
        Collection<Double> timeValues = event.getSource().getXValues();
        Collection<Double> temperatureValues = event.getSource().getYValues();
        updateValuesPanel(timeValues.size());
        fillValueFields(timeValues, temperatureValues);
    }

    private void fillValueFields(Collection<Double> timeValues, Collection<Double> temperatureValues) {
        Iterator<Double> timeIterator = timeValues.iterator();
        Iterator<Double> temperatureIterator = temperatureValues.iterator();
        for (int i = 0; i < timeValuesFields.size(); i++) {
            String time = numberFormat.format(timeIterator.next());
            String temperature = numberFormat.format(temperatureIterator.next());
            timeValuesFields.get(i).setText(time);
            temperatureValuesFields.get(i).setText(temperature);
        }
    }

    private JPanel createCountPanel() {
        JPanel countPanel = new JPanel(new FlowLayout());
        valuesCountField = new JFormattedTextField(intFormat);
        JButton setButton = new JButton("Set values count");

        valuesCountField.setValue(defaultValuesCount);
        valuesCountField.setColumns(5);
        setButton.addActionListener(e -> {
            try {
                updateValuesPanel(getValuesCount());
            } catch (ParseException | NumberFormatException exception) {
                showNumberFormatErrorDialog();
            }
        });
        countPanel.add(valuesCountField);
        countPanel.add(setButton);
        return countPanel;
    }

    private int getValuesCount() throws ParseException, NumberFormatException {
        int count;
        String text = valuesCountField.getText();
        count = intFormat.parse(text).intValue();
        if (count <= 0 || count > MAX_VALUES_COUNT) {
            throw new NumberFormatException();
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
        JPanel outerFieldsPanel = new JPanel(new BorderLayout());
        JPanel fieldsPanel = createFieldsPanel(valuesCount);
        outerFieldsPanel.add(fieldsPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(outerFieldsPanel,
                VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 300));


        JButton initializeButton = new JButton("Initialize");
        initializeButton.addActionListener(e -> initializeInterpolation());
        valuesPanel.add(scrollPane, BorderLayout.CENTER);
        valuesPanel.add(initializeButton, BorderLayout.SOUTH);
        return valuesPanel;
    }

    private JPanel createFieldsPanel(int valuesCount) {
        JPanel fieldsPanel = new JPanel(new GridLayout(valuesCount + 1, 2));
        JPanel timeLabelPanel = new JPanel();
        JPanel temperatureLabelPanel = new JPanel();
        timeLabelPanel.add(new JLabel("Time"));
        temperatureLabelPanel.add(new JLabel("Temperature"));
        fieldsPanel.add(timeLabelPanel);
        fieldsPanel.add(temperatureLabelPanel);
        timeValuesFields = new ArrayList<>(valuesCount);
        temperatureValuesFields = new ArrayList<>(valuesCount);

        for (int i = 0; i < valuesCount; i++) {
            JFormattedTextField timeField = new JFormattedTextField(numberFormat);
            JFormattedTextField temperatureField = new JFormattedTextField(numberFormat);
            timeField.setColumns(5);
            temperatureField.setColumns(5);
            JPanel timePanel = new JPanel();
            JPanel temperaturePanel = new JPanel();
            timePanel.add(timeField);
            temperaturePanel.add(temperatureField);
            timeValuesFields.add(timeField);
            temperatureValuesFields.add(temperatureField);
            fieldsPanel.add(timePanel);
            fieldsPanel.add(temperaturePanel);
        }
        return fieldsPanel;
    }

    private void initializeInterpolation() {
        try {
            List<Double> timeStamps = parseFields(timeValuesFields);
            List<Double> temperatures = parseFields(temperatureValuesFields);
            MainFrame.getInterpolation().initialize(timeStamps, temperatures);
            getParent().repaint();
        } catch (ParseException e) {
            showNumberFormatErrorDialog();
        }
    }

    private List<Double> parseFields(Collection<JTextField> fields) throws ParseException {
        List<Double> values = new ArrayList<>(fields.size());
        for (JTextField f : fields) {
            values.add(Parser.parseField(f, numberFormat));
        }
        return values;
    }

    private void showNumberFormatErrorDialog() {
        JOptionPane.showMessageDialog(
                this,
                "Error. Wrong number format.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

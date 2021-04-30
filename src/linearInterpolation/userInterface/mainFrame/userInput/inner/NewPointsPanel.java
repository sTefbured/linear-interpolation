package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.event.ObjectUpdateEvent;
import linearInterpolation.model.listener.ObjectUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * NewPointsPanel is an extension of JPanel that contains UI components
 * for calculating function values and list of result points.
 * Calculation is performed by entering an integer value into test field
 * and pressing "Add" button. Result point will be displayed on chart and
 * its values will be shown in list.
 *
 * @author Kotikov S.G.
 */
public class NewPointsPanel extends JPanel implements ObjectUpdateListener {
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final JFormattedTextField timeField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JList<Point2D.Double> addedPointsList;

    public NewPointsPanel() {
        MainFrame.getInterpolation().addObjectUpdateListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel timeFieldPanel = new JPanel(new FlowLayout());
        timeField = createTimeField();
        addButton = createAddButton();
        deleteButton = createDeleteButton();
        addedPointsList = new JList<>();
        addedPointsList.setModel(createListModel());
        setComponentsEnabled(false);
        timeFieldPanel.add(timeField);
        timeFieldPanel.add(addButton);
        timeFieldPanel.add(deleteButton);
        add(timeFieldPanel);
        add(createAddedPointsListPanel());
        setBorder(BorderFactory.createTitledBorder("Enter time"));
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        if (MainFrame.getInterpolation().isInitialized()) {
            setComponentsEnabled(true);
        }
        addedPointsList.setModel(createListModel());
    }

    private JFormattedTextField createTimeField() {
        JFormattedTextField field = new JFormattedTextField(numberFormat);
        field.setColumns(5);
        return field;
    }

    private JButton createAddButton() {
        JButton button = new JButton("Add");
        button.addActionListener(e -> {
            try {
                MainFrame.getInterpolation().addPoint(parseField(timeField));
            } catch (ParseException parseException) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error. Wrong number format.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return button;
    }

    private JButton createDeleteButton() {
        JButton button = new JButton("Delete");
        button.addActionListener(e -> {
            List<Point2D.Double> selectedPoints = addedPointsList.getSelectedValuesList();
            Interpolation interpolation = MainFrame.getInterpolation();
            selectedPoints.forEach(p -> interpolation.removePoint(p.x, p.y));
        });
        return button;
    }

    private JPanel createAddedPointsListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(addedPointsList,
                VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private DefaultListModel<Point2D.Double> createListModel() {
        Interpolation interpolation = MainFrame.getInterpolation();
        Iterable<Double> timeValues = interpolation.getXInterpolated();
        Iterable<Double> temperatureValues = interpolation.getYInterpolated();
        Iterator<Double> temperatureIterator = temperatureValues.iterator();
        DefaultListModel<Point2D.Double> dataModel = new DefaultListModel<>();
        timeValues.forEach(x -> {
            Point2D.Double point = new Point2D.Double(x, temperatureIterator.next()) {
                @Override
                public String toString() {
                    return String.format("Time: %.3f  Temperature: %.3f", getX(), getY());
                }
            };
            dataModel.add(dataModel.size(), point);
        });
        return dataModel;
    }

    private void setComponentsEnabled(boolean enabled) {
        timeField.setEditable(enabled);
        final String tooltipText = "You must input initial values first.";
        timeField.setToolTipText(enabled ? "" : tooltipText);
        addButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    // TODO: move to utils class
    private Double parseField(JTextField field) throws ParseException {
        String text = field.getText();
        return numberFormat.parse(text).doubleValue();
    }
}

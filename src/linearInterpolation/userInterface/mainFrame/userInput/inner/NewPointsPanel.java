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

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * NewPointsPanel is an extension of JPanel that contains UI components
 * for calculating function values and list of result points.
 * Calculation is performed by entering an integer value into test field
 * and pressing "Add" button. Result point will be displayed on chart and
 * its values will be shown in JList.
 *
 * @author Kotikov S.G.
 */
public class NewPointsPanel extends JPanel implements ObjectUpdateListener {
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final JFormattedTextField xField;
    private final JButton addButton;
    private final JList<Point2D.Double> addedPoints;

    public NewPointsPanel() {
        MainFrame.getInterpolation().addObjectUpdateListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel xFieldPanel = new JPanel(new FlowLayout());
        xField = createXField();
        addButton = createAddButton();
        addedPoints = new JList<>();
        addedPoints.setModel(createListModel());
        JPanel addedPointsPanel = createAddedPointsList();
        setComponentsEnabled(false);
        xFieldPanel.add(xField);
        xFieldPanel.add(addButton);
        add(xFieldPanel);
        add(addedPointsPanel);
        setBorder(BorderFactory.createTitledBorder("Enter X"));
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        if (MainFrame.getInterpolation().isInitialized()) {
            setComponentsEnabled(true);
        }
        addedPoints.setModel(createListModel());
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
                JOptionPane.showMessageDialog(
                        this,
                        "Error. Wrong number format.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return button;
    }

    private JPanel createAddedPointsList() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(addedPoints,
                VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private DefaultListModel<Point2D.Double> createListModel() {
        Interpolation interpolation = MainFrame.getInterpolation();
        Iterable<Double> xValues = interpolation.getXInterpolated();
        Iterable<Double> yValues = interpolation.getYInterpolated();
        Iterator<Double> yIterator = yValues.iterator();
        DefaultListModel<Point2D.Double> dataModel = new DefaultListModel<>();
        xValues.forEach(x -> {
            Point2D.Double point = new Point2D.Double(x, yIterator.next()) {
                @Override
                public String toString() {
                    return String.format("X: %.3f  Y: %.3f", getX(), getY());
                }
            };
            dataModel.add(dataModel.size(), point);
        });
        return dataModel;
    }

    private void setComponentsEnabled(boolean enabled) {
        xField.setEditable(enabled);
        final String tooltipText = "You must input initial values first.";
        xField.setToolTipText(enabled ? "" : tooltipText);
        addButton.setEnabled(enabled);
        addedPoints.setEnabled(enabled);
    }

    // TODO: move to utils class
    private Double parseField(JTextField field) throws ParseException {
        String text = field.getText();
        return numberFormat.parse(text).doubleValue();
    }
}

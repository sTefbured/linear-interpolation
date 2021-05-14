package linearInterpolation.userInterface.mainFrame.userInput.inner;

import linearInterpolation.model.interpolation.Interpolation;
import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;
import linearInterpolation.userInterface.mainFrame.userInput.util.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

/**
 * <code>NewPointsPanel</code> is an extension of <code>JPanel</code> that contains UI components
 * for calculating function values and list of result points.
 * Calculation is performed by entering a decimal value into text field
 * and pressing "Add" button. Result point will be displayed in the list.
 * <p>
 * This panel implements <code>InterpolationUpdateListener</code>, because it needs to be updated,
 * when new points are created in current <code>Interpolation</code> object.
 *
 * @author Kotikov S.G.
 * @see InterpolationUpdateListener
 * @see Interpolation
 */
public class NewPointsPanel extends JPanel implements InterpolationUpdateListener {
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private final JFormattedTextField timeField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JList<Point2D.Double> addedPointsList;

    /**
     * Creates a <code>NewPointsPanel</code> object and adds it to listeners list of
     * current <code>Interpolation</code>. In result panel contains text field for time values,
     * add/delete buttons and <code>JList</code> of added points.
     */
    public NewPointsPanel() {
        // Add the panel as a listener to current Interpolation object
        MainFrame.getInterpolation().addInterpolationUpdateListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel timeFieldPanel = new JPanel(new FlowLayout());
        timeField = createTimeField();
        addButton = createAddButton();
        deleteButton = createDeleteButton();
        addedPointsList = new JList<>();
        addedPointsList.setModel(createListModel(MainFrame.getInterpolation()));
        setComponentsEnabled(false);
        timeFieldPanel.add(timeField);
        timeFieldPanel.add(addButton);
        timeFieldPanel.add(deleteButton);
        add(timeFieldPanel);
        add(createAddedPointsListPanel());
        setBorder(BorderFactory.createTitledBorder("Ввод времени"));
    }

    /**
     * Updates <code>JList</code> of added points and if current <code>interpolation</code> object
     * is initialized, sets input components enabled.
     *
     * @param event <code>InterpolationUpdateEvent</code> object from source interpolation
     */
    @Override
    public void interpolationUpdated(InterpolationUpdateEvent event) {
        if (event.getSource().isInitialized()) {
            setComponentsEnabled(true);
        }

        addedPointsList.setModel(createListModel(event.getSource()));
    }

    private DefaultListModel<Point2D.Double> createListModel(Interpolation interpolation) {
        Iterable<Double> timeValues = interpolation.getXInterpolated();
        Iterable<Double> temperatureValues = interpolation.getYInterpolated();
        Iterator<Double> temperatureIterator = temperatureValues.iterator();
        DefaultListModel<Point2D.Double> dataModel = new DefaultListModel<>();
        timeValues.forEach(x -> {
            Point2D.Double point = new Point2D.Double(x, temperatureIterator.next()) {
                @Override
                public String toString() {
                    return String.format("Время: %.3f Температура: %.3f", getX(), getY());
                }
            };
            dataModel.add(dataModel.size(), point);
        });
        return dataModel;
    }

    private JFormattedTextField createTimeField() {
        JFormattedTextField field = new JFormattedTextField(numberFormat);
        field.setColumns(5);
        return field;
    }

    private JButton createAddButton() {
        JButton button = new JButton("ОК");
        button.addActionListener(e -> {
            try {
                double timeValue = Parser.parseField(timeField, numberFormat);
                MainFrame.getInterpolation().addPoint(timeValue);
            } catch (ParseException parseException) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ошибка. Неверный числовой формат.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return button;
    }

    private JButton createDeleteButton() {
        JButton button = new JButton("Удалить");
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
                VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setComponentsEnabled(boolean enabled) {
        timeField.setEditable(enabled);
        final String tooltipText = "Сначала вы должны ввести начальные значения.";
        timeField.setToolTipText(enabled ? "" : tooltipText);
        addButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }
}

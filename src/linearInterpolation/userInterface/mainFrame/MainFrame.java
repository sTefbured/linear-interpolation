package linearInterpolation.userInterface.mainFrame;

import linearInterpolation.model.interpolation.Interpolation;
import linearInterpolation.model.interpolation.TimeTemperatureInterpolation;
import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;
import linearInterpolation.userInterface.mainFrame.chart.InterpolationChartPanel;
import linearInterpolation.userInterface.mainFrame.menu.MenuBar;
import linearInterpolation.userInterface.mainFrame.userInput.InputPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A <code>MainFrame</code> object is an extended version of <code>JFrame</code> that adds
 * panels and menu bar on the frame. Used as the main window of the application.
 * Provides with UI components for performing interpolation operations.
 *
 * @author Kotikov S.G.
 */
public class MainFrame extends JFrame {
    private static Interpolation interpolation;
    private static InputPanel inputPanel;
    private static InterpolationChartPanel chartPanel;

    /**
     * Creates frame with menu bar, <code>InputPanel</code> and <code>InterpolationChartPanel</code>.
     * <code>InputPanel</code> has UI components for operations.
     * Calculations result is shown on <code>InterpolationChartPanel</code>.
     *
     * @see InputPanel
     * @see InterpolationChartPanel
     */
    public MainFrame() {
        super("Linear interpolation coursework");
        interpolation = new TimeTemperatureInterpolation();
        inputPanel = new InputPanel();
        chartPanel = new InterpolationChartPanel();
        add(inputPanel);
        add(chartPanel);
        setLayout(new GridLayout(1, 2));
        setJMenuBar(new MenuBar(this));
        setMinimumSize(new Dimension(960, 540));
        setSize(getMinimumSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Sets a new value for <code>interpolation</code> field, and adds listeners to it.
     *
     * @param interpolation new instance of Interpolation
     * @see InterpolationUpdateListener
     */
    public static void setInterpolation(Interpolation interpolation) {
        MainFrame.interpolation = interpolation;
        interpolation.addInterpolationUpdateListener(inputPanel.getInitializingPanel());
        interpolation.addInterpolationUpdateListener(inputPanel.getNewPointsPanel());
        interpolation.addInterpolationUpdateListener(chartPanel);
        InterpolationUpdateEvent event = new InterpolationUpdateEvent(interpolation);
        interpolation.notifyInterpolationUpdateListeners(event);
    }

    /**
     * @return <code>Interpolation</code> instance
     */
    public static Interpolation getInterpolation() {
        return interpolation;
    }
}

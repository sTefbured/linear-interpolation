package linearInterpolation.userInterface.mainFrame;

import linearInterpolation.model.interpolation.Interpolation;
import linearInterpolation.model.interpolation.TimeTemperatureInterpolation;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;
import linearInterpolation.userInterface.mainFrame.chart.InterpolationChartPanel;
import linearInterpolation.userInterface.mainFrame.menu.MenuBar;
import linearInterpolation.userInterface.mainFrame.userInput.InputPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A <code>MainFrame</code> object is an extended version of <code>JFrame</code> that adds
 * panels and menu bar on the frame. Contains <code>Interpolation</code> object as a field.
 *
 * @author Kotikov S.G.
 */
public class MainFrame extends JFrame {
    private static Interpolation interpolation;
    private static InputPanel inputPanel;
    private static InterpolationChartPanel chartPanel;

    /**
     * Creates frame with menu bar, input panel and chart panel.
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
        interpolation.notifyObjectUpdateListeners();
    }

    /**
     * @return <code>Interpolation</code> instance
     */
    public static Interpolation getInterpolation() {
        return interpolation;
    }
}

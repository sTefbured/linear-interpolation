package linearInterpolation.userInterface.mainFrame;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.LinearInterpolation;
import linearInterpolation.model.listener.InterpolationUpdateListener;
import linearInterpolation.userInterface.mainFrame.chart.InterpolationChartPanel;
import linearInterpolation.userInterface.mainFrame.menu.MenuBar;
import linearInterpolation.userInterface.mainFrame.userInput.InputPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A MainFrame object is an extended version of JFrame that adds
 * panels and menu bar on the frame. Contains Interpolation object as a field.
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
        interpolation = new LinearInterpolation();
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
     * Sets a new value for interpolation field, and adds listeners to it.
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
     * Get current Interpolation instance
     *
     * @return Interpolation instance
     */
    public static Interpolation getInterpolation() {
        return interpolation;
    }
}

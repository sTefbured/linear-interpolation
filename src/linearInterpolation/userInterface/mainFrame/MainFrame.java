package linearInterpolation.userInterface.mainFrame;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.LinearInterpolation;
import linearInterpolation.userInterface.mainFrame.chart.InterpolationChartPanel;
import linearInterpolation.userInterface.mainFrame.menu.MenuBar;
import linearInterpolation.userInterface.mainFrame.userInput.InputPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static Interpolation interpolation;
    private static InputPanel inputPanel;
    private static InterpolationChartPanel chartPanel;

    public MainFrame() {
        super("Linear interpolation coursework");
        interpolation = new LinearInterpolation();

        inputPanel = new InputPanel();
        chartPanel = new InterpolationChartPanel();
        add(inputPanel);
        add(chartPanel);
        setLayout(new GridLayout(1, 2));
        setJMenuBar(new MenuBar(this));
        setSize(960, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void setInterpolation(Interpolation interpolation) {
        MainFrame.interpolation = interpolation;
        interpolation.addObjectUpdateListener(inputPanel.getNewPointsPanel());
        interpolation.addObjectUpdateListener(chartPanel);
        interpolation.notifyObjectUpdateListeners();
    }

    public static Interpolation getInterpolation() {
        return interpolation;
    }
}

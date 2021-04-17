package linearInterpolation.userInterface.panels;

import linearInterpolation.model.Interpolation;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

public class GraphPanel extends JPanel {
    private Interpolation interpolation;

    private JFreeChart graph;
    private Plot plot;

    public GraphPanel(Interpolation interpolation) {
        this.interpolation = interpolation;
        plot = new XYPlot();
        //graph = new JFreeChart();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (interpolation.getXValues() == null || interpolation.getYValues() == null) {
            return;
        }
        Collection<Double> xValues = interpolation.getXValues();
        Collection<Double> yValues = interpolation.getYValues();
        Iterator<Double> xIterator = xValues.iterator();
        Iterator<Double> yIterator = yValues.iterator();
        while (xIterator.hasNext() && yIterator.hasNext()) {
            int x = xIterator.next().intValue();
            int y = yIterator.next().intValue();
            g.fillOval(x - 2, y - 2, 4, 4);
        }
        g.drawLine(0, ((int) interpolation.calculateTemperature(0)), 200,
                (int)interpolation.calculateTemperature(200));
    }
}

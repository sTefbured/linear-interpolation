package linearInterpolation.userInterface.panels;

import linearInterpolation.model.Interpolation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class GraphPanel extends JPanel implements Observer {
    private final int INITIAL_POINTS_INDEX = 0;
    private final int INTERPOLATED_LINE_INDEX = 1;
    private final int INTERPOLATED_POINTS_INDEX = 2;
    private final String INITIAL_POINTS_KEY = "Initial points";
    private final String INTERPOLATED_LINE_KEY = "Interpolated line function";
    private final String INTERPOLATED_POINTS_KEY = "Interpolated points";

    private final Interpolation interpolation;

    DefaultXYDataset dataset;
    private XYSeries initialPointsSeries;
    private XYSeries interpolatedLineSeries;
    private XYSeries interpolatedPointsSeries;
    private final JFreeChart chart;

    public GraphPanel(Interpolation interpolation) {
        this.interpolation = interpolation;
        interpolation.addObserver(this);
        dataset = createDataset();
        chart = createChart(dataset);
        ChartPanel panel = new ChartPanel(chart);
        EventListener[] listeners = panel.getListeners(MouseListener.class);
        for (EventListener listener : listeners) {
            panel.removeMouseListener((MouseListener) listener);
        }

        setLayout(new GridLayout(1, 1));
        add(panel);
    }

    private DefaultXYDataset createDataset() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        initialPointsSeries = new XYSeries(INITIAL_POINTS_KEY);
        interpolatedLineSeries = new XYSeries(INTERPOLATED_LINE_KEY);
        interpolatedPointsSeries = new XYSeries(INTERPOLATED_POINTS_KEY);
        dataset.addSeries(INITIAL_POINTS_KEY,
                initialPointsSeries.toArray());
        dataset.addSeries(INTERPOLATED_LINE_KEY,
                interpolatedLineSeries.toArray());
        dataset.addSeries(INTERPOLATED_POINTS_KEY,
                interpolatedPointsSeries.toArray());
        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("Cooling function",
                "time, min", "temperature, K", dataset);
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = chart.getXYPlot();
        configurePlot(plot);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            configureRenderer((XYLineAndShapeRenderer) r);
        }
        return chart;
    }

    private void configurePlot(XYPlot plot) {
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainMinorGridlinePaint(Color.BLUE);
        plot.setRangeMinorGridlinePaint(Color.BLUE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainCrosshairStroke(new BasicStroke(3));
        plot.setRangeCrosshairStroke(new BasicStroke(3));
        plot.setDomainCrosshairPaint(new Color(0, 0, 0, 100));
        plot.setRangeCrosshairPaint(new Color(0, 0, 0, 100));
        plot.setDomainCrosshairLockedOnData(true);
        plot.setRangeCrosshairLockedOnData(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
    }

    private void configureRenderer(XYLineAndShapeRenderer renderer) {
        // Configure renderer for initial points
        renderer.setSeriesFillPaint(INITIAL_POINTS_INDEX, Color.BLUE);
        renderer.setSeriesPaint(INITIAL_POINTS_INDEX, Color.BLACK);
        renderer.setUseFillPaint(true);
        renderer.setSeriesShape(INITIAL_POINTS_INDEX,
                new Ellipse2D.Double(-5, -5, 10, 10));
        renderer.setSeriesLinesVisible(INITIAL_POINTS_INDEX, false);
        renderer.setSeriesShapesVisible(INITIAL_POINTS_INDEX, true);
        renderer.setSeriesShapesFilled(INITIAL_POINTS_INDEX, true);

        // Configure renderer for interpolated line
        renderer.setSeriesStroke(INTERPOLATED_LINE_INDEX, new BasicStroke(5));

        // TODO: Configure renderer for interpolated points
    }

    @Override
    public void update(Observable o, Object arg) {
        Collection<Double> xValues = interpolation.getXValues();
        Collection<Double> yValues = interpolation.getYValues();

        // Remove old values
        initialPointsSeries.clear();
        interpolatedLineSeries.clear();
        interpolatedPointsSeries.clear();

        // Add new values
        Iterator<Double> yIterator = yValues.iterator();
        xValues.forEach(x -> initialPointsSeries.add(x, yIterator.next()));
        double y1 = interpolation.calculateFunctionValue(xValues.stream().min(Double::compare).get());
        double y2 = interpolation.calculateFunctionValue(xValues.stream().max(Double::compare).get());
        interpolatedLineSeries.add(xValues.stream().min(Double::compare).get().doubleValue(), y1);
        interpolatedLineSeries.add(xValues.stream().max(Double::compare).get().doubleValue(), y2);

        // TODO: decide how to add interpolated points

        dataset.removeSeries(INITIAL_POINTS_KEY);
        dataset.removeSeries(INTERPOLATED_LINE_KEY);
        dataset.removeSeries(INTERPOLATED_POINTS_KEY);
        dataset.addSeries(INITIAL_POINTS_KEY,
                initialPointsSeries.toArray());
        dataset.addSeries(INTERPOLATED_LINE_KEY,
                interpolatedLineSeries.toArray());
        dataset.addSeries(INTERPOLATED_POINTS_KEY,
                interpolatedLineSeries.toArray());
        repaint();
    }
}

package linearInterpolation.userInterface.mainFrame.chart;

import linearInterpolation.model.Interpolation;
import linearInterpolation.model.event.ObjectUpdateEvent;
import linearInterpolation.model.listener.ObjectUpdateListener;
import linearInterpolation.userInterface.mainFrame.MainFrame;
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

public class InterpolationChartPanel
        extends JPanel implements ObjectUpdateListener {
    public final int INTERPOLATED_POINTS_INDEX = 0;
    public final int INITIAL_POINTS_INDEX = 1;
    public final int INTERPOLATED_LINE_INDEX = 2;
    private final String INITIAL_POINTS_KEY = "Initial points";
    private final String INTERPOLATED_LINE_KEY = "Interpolated line function";
    private final String INTERPOLATED_POINTS_KEY = "Interpolated points";

    private final DefaultXYDataset dataset;

    private XYSeries initialPointsSeries;
    private XYSeries interpolatedLineSeries;
    private XYSeries interpolatedPointsSeries;

    public InterpolationChartPanel() {
        MainFrame.getInterpolation().addObjectUpdateListener(this);
        dataset = createDataset();
        JFreeChart chart = createChart(dataset);
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
                "time, hour", "temperature, K", dataset);
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
        // Configure renderer for interpolated points
        renderer.setSeriesFillPaint(INTERPOLATED_POINTS_INDEX, Color.YELLOW);
        configureRendererForPoints(renderer, INTERPOLATED_POINTS_INDEX);

        // Configure renderer for initial points
        renderer.setSeriesFillPaint(INITIAL_POINTS_INDEX, Color.BLUE);
        configureRendererForPoints(renderer, INITIAL_POINTS_INDEX);

        // Configure renderer for interpolated line
        renderer.setSeriesStroke(INTERPOLATED_LINE_INDEX, new BasicStroke(5));
    }

    private void configureRendererForPoints(XYLineAndShapeRenderer renderer,
                                            int seriesIndex) {
        renderer.setSeriesPaint(seriesIndex, Color.BLACK);
        renderer.setUseFillPaint(true);
        renderer.setSeriesShape(seriesIndex,
                new Ellipse2D.Double(-5, -5, 10, 10));
        renderer.setSeriesLinesVisible(seriesIndex, false);
        renderer.setSeriesShapesVisible(seriesIndex, true);
        renderer.setSeriesShapesFilled(seriesIndex, true);
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        Interpolation interpolation = MainFrame.getInterpolation();
        Collection<Double> xValues = interpolation.getXValues();
        Collection<Double> yValues = interpolation.getYValues();
        Collection<Double> interpolatedX = interpolation.getXInterpolated();
        Collection<Double> interpolatedY = interpolation.getYInterpolated();

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

        Iterator<Double> yInterpolatedIterator = interpolatedY.iterator();
        interpolatedX.forEach(x -> interpolatedPointsSeries.add(x, yInterpolatedIterator.next()));

        dataset.removeSeries(INITIAL_POINTS_KEY);
        dataset.removeSeries(INTERPOLATED_LINE_KEY);
        dataset.removeSeries(INTERPOLATED_POINTS_KEY);

        dataset.addSeries(INTERPOLATED_POINTS_KEY,
                interpolatedPointsSeries.toArray());
        dataset.addSeries(INITIAL_POINTS_KEY,
                initialPointsSeries.toArray());
        dataset.addSeries(INTERPOLATED_LINE_KEY,
                interpolatedLineSeries.toArray());
        repaint();
    }
}

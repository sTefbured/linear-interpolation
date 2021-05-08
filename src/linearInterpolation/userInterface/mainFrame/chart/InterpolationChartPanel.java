package linearInterpolation.userInterface.mainFrame.chart;

import linearInterpolation.model.interpolation.LinearInterpolation;
import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;
import linearInterpolation.model.interpolation.Interpolation;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;
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
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public class InterpolationChartPanel extends JPanel implements InterpolationUpdateListener {
    public final int INTERPOLATED_POINTS_INDEX = 0;
    public final int INITIAL_POINTS_INDEX = 1;
    public final int INTERPOLATED_LINE_INDEX = 2;
    private final String INITIAL_POINTS_KEY = "Initial points";
    private final String INTERPOLATED_LINE_KEY = "Interpolated line function";
    private final String INTERPOLATED_POINTS_KEY = "Interpolated points";

    private final DefaultXYDataset dataset;
    private final XYSeries initialPointsSeries;
    private final XYSeries interpolatedLineSeries;
    private final XYSeries interpolatedPointsSeries;

    public InterpolationChartPanel() {
        MainFrame.getInterpolation().addInterpolationUpdateListener(this);
        dataset = new DefaultXYDataset();
        initialPointsSeries = new XYSeries(INITIAL_POINTS_KEY);
        interpolatedLineSeries = new XYSeries(INTERPOLATED_LINE_KEY);
        interpolatedPointsSeries = new XYSeries(INTERPOLATED_POINTS_KEY);
        addAllSeries();
        JFreeChart chart = createChart(dataset);
        ChartPanel panel = new ChartPanel(chart);
        EventListener[] listeners = panel.getListeners(MouseListener.class);
        for (EventListener listener : listeners) {
            panel.removeMouseListener((MouseListener) listener);
        }
        setLayout(new GridLayout(1, 1));
        add(panel);
    }

    @Override
    public void interpolationUpdated(InterpolationUpdateEvent event) {
        removeAllSeries();
        clearAllSeries();
        initializeAllSeries(event.getSource());
        addAllSeries();
        repaint();
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("Cooling function",
                "time, hour", "temperature, K", dataset);
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = chart.getXYPlot();
        configurePlot(plot);

        XYItemRenderer renderer = plot.getRenderer();
        if (renderer instanceof XYLineAndShapeRenderer) {
            configureRenderer((XYLineAndShapeRenderer) renderer);
        }
        return chart;
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

    private void configureRendererForPoints(XYLineAndShapeRenderer renderer, int seriesIndex) {
        renderer.setSeriesPaint(seriesIndex, Color.BLACK);
        renderer.setUseFillPaint(true);
        renderer.setSeriesShape(seriesIndex, new Ellipse2D.Double(-5, -5, 10, 10));
        renderer.setSeriesLinesVisible(seriesIndex, false);
        renderer.setSeriesShapesVisible(seriesIndex, true);
        renderer.setSeriesShapesFilled(seriesIndex, true);
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

    private void initializeAllSeries(Interpolation interpolation) {
        initializeLineSeries((LinearInterpolation) interpolation);
        initializePointSeries(initialPointsSeries,
                interpolation.getXValues(), interpolation.getYValues());
        initializePointSeries(interpolatedPointsSeries,
                interpolation.getXInterpolated(), interpolation.getYInterpolated());
    }

    private void initializeLineSeries(LinearInterpolation interpolation) {
        // Find minimum and maximum time value among new and initial time values in interpolation.
        double minTimeValue = Collections.min(interpolation.getXValues());
        double maxTimeValue = Collections.max(interpolation.getXValues());
        Collection<Double> xInterpolated = interpolation.getXInterpolated();
        if (!xInterpolated.isEmpty()) {
            double interpolatedMinTime = Collections.min(xInterpolated);
            double interpolatedMaxTime = Collections.max(xInterpolated);
            minTimeValue = Math.min(minTimeValue, interpolatedMinTime);
            maxTimeValue = Math.max(maxTimeValue, interpolatedMaxTime);
        }

        // If having zero temperature, add zero point of the function.
        double zeroOfTheFunction = -interpolation.getCoefficientB() / interpolation.getCoefficientA();
        if (minTimeValue < zeroOfTheFunction && maxTimeValue > zeroOfTheFunction) {
            interpolatedLineSeries.add(zeroOfTheFunction, 0);
        }

        // Set range of the line
        double temperature1 = interpolation.calculateFunctionValue(minTimeValue);
        double temperature2 = interpolation.calculateFunctionValue(maxTimeValue);
        interpolatedLineSeries.add(minTimeValue, temperature1);
        interpolatedLineSeries.add(maxTimeValue, temperature2);
    }

    private void initializePointSeries(XYSeries pointSeries, List<Double> times, List<Double> temperatures) {
        Iterator<Double> temperatureIterator = temperatures.iterator();
        times.forEach(x -> pointSeries.add(x, temperatureIterator.next()));
    }

    private void clearAllSeries() {
        initialPointsSeries.clear();
        interpolatedLineSeries.clear();
        interpolatedPointsSeries.clear();
    }

    private void removeAllSeries() {
        dataset.removeSeries(INITIAL_POINTS_KEY);
        dataset.removeSeries(INTERPOLATED_LINE_KEY);
        dataset.removeSeries(INTERPOLATED_POINTS_KEY);
    }

    private void addAllSeries() {
        dataset.addSeries(INTERPOLATED_POINTS_KEY, interpolatedPointsSeries.toArray());
        dataset.addSeries(INITIAL_POINTS_KEY, initialPointsSeries.toArray());
        dataset.addSeries(INTERPOLATED_LINE_KEY, interpolatedLineSeries.toArray());
    }
}

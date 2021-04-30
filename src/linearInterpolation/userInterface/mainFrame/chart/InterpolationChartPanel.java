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
import java.util.Collections;
import java.util.EventListener;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class InterpolationChartPanel extends JPanel implements ObjectUpdateListener {
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
        MainFrame.getInterpolation().addObjectUpdateListener(this);
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

    private void configureRendererForPoints(XYLineAndShapeRenderer renderer, int seriesIndex) {
        renderer.setSeriesPaint(seriesIndex, Color.BLACK);
        renderer.setUseFillPaint(true);
        renderer.setSeriesShape(seriesIndex, new Ellipse2D.Double(-5, -5, 10, 10));
        renderer.setSeriesLinesVisible(seriesIndex, false);
        renderer.setSeriesShapesVisible(seriesIndex, true);
        renderer.setSeriesShapesFilled(seriesIndex, true);
    }

    @Override
    public void update(ObjectUpdateEvent event) {
        removeAllSeries();
        clearAllSeries();
        initializeAllSeries();
        addAllSeries();
        repaint();
    }

    private void initializeAllSeries() {
        Interpolation interpolation = MainFrame.getInterpolation();
        double minTimeValue = Collections.min(interpolation.getXValues());
        double maxTimeValue = Collections.max(interpolation.getXValues());
        double time1;
        double time2;
        try {
            double interpolatedMinTime = Collections.min(interpolation.getXInterpolated());
            double interpolatedMaxTime = Collections.max(interpolation.getXInterpolated());
            time1 = Math.min(minTimeValue, interpolatedMinTime);
            time2 = Math.max(maxTimeValue, interpolatedMaxTime);
        } catch (NoSuchElementException exception) {
            time1 = minTimeValue;
            time2 = maxTimeValue;
        }
        double temperature1 = interpolation.calculateFunctionValue(time1);
        double temperature2 = interpolation.calculateFunctionValue(time2);
        interpolatedLineSeries.add(time1, temperature1);
        interpolatedLineSeries.add(time2, temperature2);
        Iterator<Double> temperatureIterator = interpolation.getYValues().iterator();
        interpolation.getXValues().forEach(x -> initialPointsSeries.add(x, temperatureIterator.next()));
        Iterator<Double> temperatureInterpolatedIterator = interpolation.getYInterpolated().iterator();
        interpolation.getXInterpolated()
                .forEach(x -> interpolatedPointsSeries.add(x, temperatureInterpolatedIterator.next()));
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

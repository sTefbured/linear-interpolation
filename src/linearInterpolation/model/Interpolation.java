package linearInterpolation.model;

import linearInterpolation.model.event.InterpolationUpdateEvent;
import linearInterpolation.model.listener.InterpolationUpdateListener;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Interpolation implements Serializable {
    private static final long serialVersionUID = 360822176844365239L;

    private transient EventListenerList listenersList;
    private List<Double> xValues;
    private List<Double> yValues;
    private final List<Double> xInterpolated;
    private final List<Double> yInterpolated;
    private double coefficientA;
    private double coefficientB;

    public Interpolation() {
        listenersList = new EventListenerList();
        xInterpolated = new ArrayList<>();
        yInterpolated = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    public void initialize(List<Double> xValues, List<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            throw new IllegalArgumentException("X count must be equal to Y count");
        }
        this.xValues = xValues;
        this.yValues = yValues;
        xInterpolated.clear();
        yInterpolated.clear();
        initializeCoefficients();
        notifyObjectUpdateListeners();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateFunctionValue(double xValue);

    public void addPoint(double x) {
        if (xInterpolated.contains(x)) {
            return;
        }
        xInterpolated.add(x);
        yInterpolated.add(calculateFunctionValue(x));
        notifyObjectUpdateListeners();
    }

    public void removePoint(double x, double y) {
        int index = xInterpolated.indexOf(x);
        if ((index >= 0) && (y == yInterpolated.get(index))) {
            xInterpolated.remove(index);
            yInterpolated.remove(index);
        }
        notifyObjectUpdateListeners();
    }

    public void addInterpolationUpdateListener(InterpolationUpdateListener listener) {
        if (listenersList == null) {
            listenersList = new EventListenerList();
        }
        listenersList.add(InterpolationUpdateListener.class, listener);
    }

    public void notifyObjectUpdateListeners() {
        InterpolationUpdateEvent event = new InterpolationUpdateEvent(this);
        InterpolationUpdateListener[] listeners = listenersList.getListeners(InterpolationUpdateListener.class);
        for (InterpolationUpdateListener listener : listeners) {
            listener.interpolationUpdated(event);
        }
    }

    public boolean isInitialized() {
        return xValues != null && yValues != null;
    }

    public boolean isEmpty() {
        return xValues.isEmpty() || yValues.isEmpty();
    }

    public List<Double> getXValues() {
        return xValues;
    }

    public List<Double> getYValues() {
        return yValues;
    }

    public List<Double> getXInterpolated() {
        return xInterpolated;
    }

    public List<Double> getYInterpolated() {
        return yInterpolated;
    }

    public double getCoefficientA() {
        return coefficientA;
    }

    protected void setCoefficientA(double coefficientA) {
        this.coefficientA = coefficientA;
    }

    public double getCoefficientB() {
        return coefficientB;
    }

    protected void setCoefficientB(double coefficientB) {
        this.coefficientB = coefficientB;
    }
}

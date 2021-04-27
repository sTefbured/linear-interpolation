package linearInterpolation.model;

import linearInterpolation.model.event.ObjectUpdateEvent;
import linearInterpolation.model.listener.ObjectUpdateListener;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.*;

public abstract class Interpolation implements Serializable {
    private static final long serialVersionUID = 360822176844365239L;

    private transient EventListenerList listeners;
    private Collection<Double> xValues;
    private Collection<Double> yValues;
    private final Collection<Double> xInterpolated;
    private final Collection<Double> yInterpolated;
    private double coefficientA;
    private double coefficientB;

    public Interpolation() {
        xInterpolated = new ArrayList<>();
        yInterpolated = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    public void initialize(Collection<Double> xValues,
                           Collection<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            throw new IllegalArgumentException("X count must be equal"
                    + "to Y count");
        }
        this.xValues = xValues;
        this.yValues = yValues;
        initializeCoefficients();
        notifyObjectUpdateListeners();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateFunctionValue(double xValue);

    public void addPoint(double x) {
        xInterpolated.add(x);
        yInterpolated.add(calculateFunctionValue(x));
        notifyObjectUpdateListeners();
    }

    public void addObjectUpdateListener(ObjectUpdateListener listener) {
        if (listeners == null) {
            listeners = new EventListenerList();
        }
        listeners.add(ObjectUpdateListener.class, listener);
    }

    public void notifyObjectUpdateListeners() {
        ObjectUpdateEvent event = new ObjectUpdateEvent(this);
        for (ObjectUpdateListener listener
                : listeners.getListeners(ObjectUpdateListener.class)) {
            listener.update(event);
        }
    }

    public boolean isInitialized() {
        return xValues != null && yValues != null;
    }

    public boolean isEmpty() {
        return xValues.isEmpty() || yValues.isEmpty();
    }

    public Collection<Double> getXValues() {
        return xValues;
    }

    public Collection<Double> getYValues() {
        return yValues;
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

    public Collection<Double> getXInterpolated() {
        return xInterpolated;
    }

    public Collection<Double> getYInterpolated() {
        return yInterpolated;
    }
}

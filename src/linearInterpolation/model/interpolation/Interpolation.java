package linearInterpolation.model.interpolation;

import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of an interpolation calculation tool. Calculations are executed in two steps.
 * First is setting initial x and y values by call <code>initialize</code> method.
 *
 * @author Kotikov S.G.
 */
public abstract class Interpolation implements Serializable {
    private static final long serialVersionUID = 360822176844365239L;

    private transient EventListenerList listenersList;
    private List<Double> xValues;
    private List<Double> yValues;
    private final List<Double> xInterpolated;
    private final List<Double> yInterpolated;
    private final List<Double> coefficients;

    public Interpolation() {
        listenersList = new EventListenerList();
        xInterpolated = new ArrayList<>();
        yInterpolated = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        coefficients = new ArrayList<>();
    }

    public void initialize(List<Double> xValues, List<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            final String errorMessage = "X count must be equal to Y count";
            throw new IllegalArgumentException(errorMessage);
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
        return Collections.unmodifiableList(xValues);
    }

    public List<Double> getYValues() {
        return Collections.unmodifiableList(yValues);
    }

    public List<Double> getXInterpolated() {
        return Collections.unmodifiableList(xInterpolated);
    }

    public List<Double> getYInterpolated() {
        return Collections.unmodifiableList(yInterpolated);
    }

    public List<Double> getCoefficients() {
        return Collections.unmodifiableList(coefficients);
    }

    protected void setCoefficients(double... coefficients) {
        if (!this.coefficients.isEmpty()) {
            this.coefficients.clear();
        }
        for (double coefficient : coefficients) {
            this.coefficients.add(coefficient);
        }
    }
}

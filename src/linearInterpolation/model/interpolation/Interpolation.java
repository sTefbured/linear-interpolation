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
 * The first step is setting initial <b>x</b> and <b>y</b> values by call <code>initialize</code> method.
 * The second step is calling <code>calculateFunctionValue</code> method,
 * which returns <b>y</b> value for given <b>x</b> value.
 * <p>
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

    /**
     * Initializes an instance of <code>Interpolation</code>.
     */
    public Interpolation() {
        listenersList = new EventListenerList();
        xInterpolated = new ArrayList<>();
        yInterpolated = new ArrayList<>();
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        coefficients = new ArrayList<>();
    }

    /**
     * Sets <b>x</b> and <b>y</b> values of the interpolation function
     * and calculates coefficients of the function.
     * Notifies all subscribed <code>InterpolationUpdateListener</code> objects.
     *
     * @param xValues initial <b>x</b> values.
     * @param yValues initial <b>y</b> values.
     */
    public void initialize(List<Double> xValues, List<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            final String errorMessage = "Количество значений X должно быть равно количеству значений Y";
            throw new IllegalArgumentException(errorMessage);
        }
        this.xValues = xValues;
        this.yValues = yValues;
        xInterpolated.clear();
        yInterpolated.clear();
        initializeCoefficients();
        notifyInterpolationUpdateListeners();
    }

    /**
     * Calculates coefficients of specified function.
     */
    protected abstract void initializeCoefficients();

    /**
     * Calculates interpolation function value.
     *
     * @param xValue x value of the function.
     * @return calculated <b>y</b> value of the function.
     */
    public abstract double calculateFunctionValue(double xValue);

    public void addPoint(double x) {
        if (xInterpolated.contains(x)) {
            return;
        }
        xInterpolated.add(x);
        yInterpolated.add(calculateFunctionValue(x));
        notifyInterpolationUpdateListeners();
    }

    /**
     * Removes specified point.
     *
     * @param x <b>x</b> value of the point.
     * @param y <b>y</b> value of the point
     */
    public void removePoint(double x, double y) {
        int index = xInterpolated.indexOf(x);
        if ((index >= 0) && (y == yInterpolated.get(index))) {
            xInterpolated.remove(index);
            yInterpolated.remove(index);
        }
        notifyInterpolationUpdateListeners();
    }

    /**
     * Subscribes <code>InterpolationUpdateListener</code> object to the interpolation.
     *
     * @param listener listener to be subscribed.
     */
    public void addInterpolationUpdateListener(InterpolationUpdateListener listener) {
        if (listenersList == null) {
            listenersList = new EventListenerList();
        }
        listenersList.add(InterpolationUpdateListener.class, listener);
    }

    public void notifyInterpolationUpdateListeners() {
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

    /**
     * Sets coefficients of the interpolation function.
     *
     * @param coefficients coefficients of interpolation function.
     */
    protected void setCoefficients(double... coefficients) {
        if (!this.coefficients.isEmpty()) {
            this.coefficients.clear();
        }
        for (double coefficient : coefficients) {
            this.coefficients.add(coefficient);
        }
    }
}

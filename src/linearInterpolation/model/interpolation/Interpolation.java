package linearInterpolation.model.interpolation;

import linearInterpolation.model.interpolation.event.InterpolationUpdateEvent;
import linearInterpolation.model.interpolation.listener.InterpolationUpdateListener;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Interpolation is a class used for interpolation calculations. Calculations are executed in two steps.
 * The first step is setting initial <b>x</b> and <b>y</b> values by call <code>initialize</code> method.
 * The second step is calling <code>calculateFunctionValue</code> method,
 * which returns <b>y</b> value for given <b>x</b> value.
 * <p>
 * The class must be extended to specify, which function must be represented.
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
        InterpolationUpdateEvent event = new InterpolationUpdateEvent(this);
        notifyInterpolationUpdateListeners(event);
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

    /**
     * Adds a new point with interpolated <b>y</b> value.
     *
     * @param x <b>x</b> value of the point
     */
    public void addPoint(double x) {
        if (xInterpolated.contains(x)) {
            return;
        }
        xInterpolated.add(x);
        yInterpolated.add(calculateFunctionValue(x));
        InterpolationUpdateEvent event = new InterpolationUpdateEvent(this);
        notifyInterpolationUpdateListeners(event);
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
        InterpolationUpdateEvent event = new InterpolationUpdateEvent(this);
        notifyInterpolationUpdateListeners(event);
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

    /**
     * Notifies all subscribed <code>InterpolationUpdateListeners</code>
     * about new <code>InterpolationUpdateEvent</code>.
     *
     * @param event new <code>InterpolationUpdateEvent</code>.
     * @see InterpolationUpdateEvent
     */
    public void notifyInterpolationUpdateListeners(InterpolationUpdateEvent event) {
        InterpolationUpdateListener[] listeners = listenersList.getListeners(InterpolationUpdateListener.class);
        for (InterpolationUpdateListener listener : listeners) {
            listener.interpolationUpdated(event);
        }
    }

    /**
     * @return <b>true</b> if <code>xValues</code> and <code>yValues</code> are not null.
     */
    public boolean isInitialized() {
        return xValues != null && yValues != null;
    }

    /**
     * @return <b>true</b> if <code>xValues</code> or <code>yValues</code> is empty.
     */
    public boolean isEmpty() {
        return xValues.isEmpty() || yValues.isEmpty();
    }

    /**
     * @return unmodifiable <b>x</b> values list.
     */
    public List<Double> getXValues() {
        return Collections.unmodifiableList(xValues);
    }

    /**
     * @return unmodifiable <b>y</b> values list.
     */
    public List<Double> getYValues() {
        return Collections.unmodifiableList(yValues);
    }

    /**
     * @return unmodifiable interpolated <b>x</b> values list.
     */
    public List<Double> getXInterpolated() {
        return Collections.unmodifiableList(xInterpolated);
    }

    /**
     * @return unmodifiable interpolated <b>y</b> values list.
     */
    public List<Double> getYInterpolated() {
        return Collections.unmodifiableList(yInterpolated);
    }

    /**
     * @return unmodifiable list of coefficients.
     */
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

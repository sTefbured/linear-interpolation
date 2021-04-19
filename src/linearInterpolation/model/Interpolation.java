package linearInterpolation.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Observable;

public abstract class Interpolation
        extends Observable implements Serializable {
    private static final long serialVersionUID = 360822176844365239L;

    private Collection<Double> xValues;
    private Collection<Double> yValues;
    private transient Collection<Double> xInterpolated;
    private transient Collection<Double> yInterpolated;
    private double coefficientA;
    private double coefficientB;

    public void initialize(Collection<Double> xValues,
                           Collection<Double> yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        initializeCoefficients();
        setChanged();
        notifyObservers();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateFunctionValue(double xValue);

    public Collection<Double> getXValues() {
        return xValues;
    }

    public Collection<Double> getYValues() {
        return yValues;
    }

    protected void setCoefficientA(double coefficientA) {
        this.coefficientA = coefficientA;
    }

    protected void setCoefficientB(double coefficientB) {
        this.coefficientB = coefficientB;
    }

    public double getCoefficientA() {
        return coefficientA;
    }

    public double getCoefficientB() {
        return coefficientB;
    }
}

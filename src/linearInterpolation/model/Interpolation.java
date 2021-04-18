package linearInterpolation.model;

import java.util.Collection;
import java.util.Observable;

public abstract class Interpolation extends Observable {
    private Collection<Double> xValues;
    private Collection<Double> yValues;
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

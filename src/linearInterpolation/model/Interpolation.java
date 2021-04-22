package linearInterpolation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;

public abstract class Interpolation
        extends Observable implements Serializable {
    private static final long serialVersionUID = 360822176844365239L;

    private Collection<Double> xValues;
    private Collection<Double> yValues;

    private Collection<Double> xInterpolated;
    private Collection<Double> yInterpolated;

    private double coefficientA;
    private double coefficientB;

    public Interpolation() {
        xInterpolated = new ArrayList<>();
        yInterpolated = new ArrayList<>();
    }

    public void initialize(Collection<Double> xValues,
                           Collection<Double> yValues) {
        if (xValues.size() != yValues.size()) {
            throw new IndexOutOfBoundsException("X count must be equal"
                    + "to Y count");
        }
        this.xValues = xValues;
        this.yValues = yValues;
        initializeCoefficients();
        setChanged();
        notifyObservers();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateFunctionValue(double xValue);

    public void addPoint(double x) {
        xInterpolated.add(x);
        yInterpolated.add(calculateFunctionValue(x));
        setChanged();
        notifyObservers();
    }

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

    public Collection<Double> getXInterpolated() {
        return xInterpolated;
    }

    public Collection<Double> getYInterpolated() {
        return yInterpolated;
    }
}

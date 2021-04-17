package linearInterpolation.model;

import java.util.Collection;

public abstract class Interpolation {
    private Collection<Double> xValues;
    private Collection<Double> yValues;
    private double coefficientA;
    private double coefficientB;

    public void initialize(Collection<Double> xValues,
                           Collection<Double> yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        initializeCoefficients();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateTemperature(double timeStamp);

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

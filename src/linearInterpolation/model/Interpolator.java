package linearInterpolation.model;

public abstract class Interpolator {
    private final double[] xValues;
    private final double[] yValues;
    private double coefficientA;
    private double coefficientB;

    public Interpolator(double[] xValues, double[] yValues) {
        this.xValues = xValues;
        this.yValues = yValues;
        initializeCoefficients();
    }

    protected abstract void initializeCoefficients();

    public abstract double calculateTemperature(double timeStamp);

    public double[] getXValues() {
        return xValues;
    }

    public double[] getYValues() {
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

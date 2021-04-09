package linearInterpolation.businessLogics;

public class LinearInterpolator extends Interpolator {
    public LinearInterpolator(double[] xValues, double[] yValues){
        super(xValues, yValues);
    }

    @Override
    protected void initializeCoefficients() {

    }

    @Override
    public double calculateTemperature(double timeStamp) {
        return getCoefficientA() * timeStamp + getCoefficientB();
    }
}

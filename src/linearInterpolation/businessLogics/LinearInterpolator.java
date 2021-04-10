package linearInterpolation.businessLogics;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class LinearInterpolator extends Interpolator {
    public LinearInterpolator(double[] xValues, double[] yValues){
        super(xValues, yValues);
    }

    @Override
    protected void initializeCoefficients() {
        DoubleStream xStream = Arrays.stream(getXValues());
        DoubleStream yStream = Arrays.stream(getYValues());
        int valuesCount = getXValues().length;
        double xSum = xStream.sum();
        double ySum = yStream.sum();
        double squaredXSum = Arrays.stream(getXValues()).map(x -> x * x).sum();
        double xYSum = multiply(getXValues(), getYValues());
        setCoefficientA((valuesCount * xYSum - xSum * ySum) / (valuesCount * squaredXSum - xSum * xSum));
        setCoefficientB((ySum - getCoefficientA() * xSum) / valuesCount);
    }

    private double multiply(double[] array1, double[] array2){
        double result = 0;
        for (int i = 0; i < array1.length; i++) {
            result += array1[i] * array2[i];
        }
        return result;
    }

    @Override
    public double calculateTemperature(double timeStamp) {
        return getCoefficientA() * timeStamp + getCoefficientB();
    }
}

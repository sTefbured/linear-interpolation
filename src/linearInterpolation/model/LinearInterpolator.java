package linearInterpolation.model;

import linearInterpolation.utils.DoubleStreamMapper;

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
        double squaredXSum = xStream.map(x -> x * x).sum();
        double xYSum = DoubleStreamMapper.crossMap(xStream, yStream, (x, y) -> x * y).sum();
        setCoefficientA((valuesCount * xYSum - xSum * ySum) / (valuesCount * squaredXSum - xSum * xSum));
        setCoefficientB((ySum - getCoefficientA() * xSum) / valuesCount);
    }

    @Override
    public double calculateTemperature(double timeStamp) {
        return getCoefficientA() * timeStamp + getCoefficientB();
    }
}

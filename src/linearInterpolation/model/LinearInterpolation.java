package linearInterpolation.model;

import linearInterpolation.model.utils.DoubleCollectionMapper;

import java.util.Collection;

public class LinearInterpolation extends Interpolation {
    @Override
    protected void initializeCoefficients() {
        Collection<Double> xValues = getXValues();
        Collection<Double> yValues = getYValues();
        int valuesCount = xValues.size();
        double squaredXSum = xValues.stream().mapToDouble(x -> x * x).sum();
        double xSum = xValues.stream().mapToDouble(x -> x).sum();
        double ySum = yValues.stream().mapToDouble(y -> y).sum();
        double xYSum = DoubleCollectionMapper.crossMap(xValues,
                yValues, (x, y) -> x * y).sum();
        setCoefficientA((valuesCount * xYSum - xSum * ySum) / (valuesCount * squaredXSum - xSum * xSum));
        setCoefficientB((ySum - getCoefficientA() * xSum) / valuesCount);
    }

    @Override
    public double calculateFunctionValue(double timeStamp) {
        return getCoefficientA() * timeStamp + getCoefficientB();
    }
}

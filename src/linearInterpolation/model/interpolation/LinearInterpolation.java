package linearInterpolation.model.interpolation;

import linearInterpolation.model.utils.DoubleCollectionMapper;

import java.util.Collection;

/**
 * <code>LinearInterpolation</code> is an extension of
 * <code>Interpolation</code> class which represents
 * linear interpolation algorithm. The target function is
 * <p>y = a * x + b</p>
 * with coefficients a and b.
 *
 * @author Kotikov S.G.
 */
public class LinearInterpolation extends Interpolation {
    private static final long serialVersionUID = -416852628108060469L;

    /**
     * Calculates value of linear function with coefficients, calculated by
     * <code>initializeCoefficients</code> method.
     *
     * @param x argument value
     * @return function value
     */
    @Override
    public double calculateFunctionValue(double x) {
        return getCoefficientA() * x + getCoefficientB();
    }

    @Override
    protected void initializeCoefficients() {
        Collection<Double> xValues = getXValues();
        Collection<Double> yValues = getYValues();
        int valuesCount = xValues.size();
        double squaredXSum = xValues.stream().mapToDouble(x -> x * x).sum();
        double xSum = xValues.stream().mapToDouble(Double::doubleValue).sum();
        double ySum = yValues.stream().mapToDouble(Double::doubleValue).sum();
        double xYSum = DoubleCollectionMapper.crossMap(xValues, yValues, (x, y) -> x * y).sum();
        double a = (valuesCount * xYSum - xSum * ySum) / (valuesCount * squaredXSum - xSum * xSum);
        double b = (ySum - a * xSum) / valuesCount;
        setCoefficients(a, b);
    }

    public double getCoefficientA() {
        return getCoefficients().get(0);
    }

    public double getCoefficientB() {
        return getCoefficients().get(1);
    }
}

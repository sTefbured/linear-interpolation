package linearInterpolation.model.interpolation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

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

    private static final int A_INDEX = 0;
    private static final int B_INDEX = 1;

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

    /**
     * Initializes coefficients of linear interpolation function using
     */
    @Override
    protected void initializeCoefficients() {
        Collection<Double> xValues = getXValues();
        Collection<Double> yValues = getYValues();
        int valuesCount = xValues.size();
        double squaredXSum = xValues.stream().mapToDouble(x -> x * x).sum();
        double xSum = xValues.stream().mapToDouble(Double::doubleValue).sum();
        double ySum = yValues.stream().mapToDouble(Double::doubleValue).sum();
        double xYSum = crossMap(xValues, yValues, (x, y) -> x * y).sum();
        double a = (valuesCount * xYSum - xSum * ySum) / (valuesCount * squaredXSum - xSum * xSum);
        double b = (ySum - a * xSum) / valuesCount;
        setCoefficients(a, b);
    }

    public double getCoefficientA() {
        return getCoefficients().get(A_INDEX);
    }

    public double getCoefficientB() {
        return getCoefficients().get(B_INDEX);
    }

    private DoubleStream crossMap(Collection<Double> first, Collection<Double> second, DoubleBinaryOperator mapper) {
        if (first.size() != second.size()) {
            final String message = "Коллекции должны иметь одинаковый размер.";
            throw new IndexOutOfBoundsException(message);
        }
        int count = first.size();
        double[] resultArray = new double[count];
        Iterator<Double> firstIterator = first.iterator();
        Iterator<Double> secondIterator = second.iterator();
        for (int i = 0; i < count; i++) {
            double firstValue = firstIterator.next();
            double secondValue = secondIterator.next();
            resultArray[i] = mapper.applyAsDouble(firstValue, secondValue);
        }
        return Arrays.stream(resultArray);
    }
}

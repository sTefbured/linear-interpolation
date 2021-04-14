package linearInterpolation.utils;

import java.util.Arrays;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

public final class DoubleStreamMapper {
    // Don't let anyone instantiate this class
    private DoubleStreamMapper(){}

    public static DoubleStream crossMap(DoubleStream firstStream,
                                        DoubleStream secondStream,
                                        DoubleBinaryOperator mapper) {
        if (firstStream.count() != secondStream.count()) {
            throw new IndexOutOfBoundsException("Streams must have the same count.");
        }
        int count = (int) firstStream.count();
        double[] resultArray = new double[count];
        for (int i = 0; i < count; i++) {
            double firstValue = firstStream.iterator().nextDouble();
            double secondValue = secondStream.iterator().nextDouble();
            resultArray[i] = mapper.applyAsDouble(firstValue, secondValue);
        }
        return Arrays.stream(resultArray);
    }
}

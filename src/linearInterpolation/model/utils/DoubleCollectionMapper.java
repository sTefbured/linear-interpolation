package linearInterpolation.model.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.DoubleStream;

public final class DoubleCollectionMapper {
    // Don't let anyone instantiate this class
    private DoubleCollectionMapper(){}

    // TODO: GET RID OF THAT
    public static DoubleStream crossMap(Collection<Double> first,
                                        Collection<Double> second,
                                        DoubleBinaryOperator mapper) {
        if (first.size() != second.size()) {
            final String message = "Collections must have the same count.";
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

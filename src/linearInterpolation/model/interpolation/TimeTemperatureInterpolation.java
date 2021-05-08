package linearInterpolation.model.interpolation;

/**
 * <code>TimeTemperatureInterpolation</code> is an extension of
 * <code>LinearInterpolation</code> which handles negative function values
 * and replaces them with zeros. Made because of minimum temperature
 * limit of 0 K.
 *
 * @author Kotikov S.G.
 * @see LinearInterpolation
 */
public class TimeTemperatureInterpolation extends LinearInterpolation {
    private static final long serialVersionUID = 4397836194211557091L;

    /**
     * Calculates the value of the temperature function at a specific point
     * in time using linear interpolation algorithm. If function value is
     * negative, returns zero, due to the minimum temperature limit.
     *
     * @param time point in time
     * @return calculated temperature.
     */
    @Override
    public double calculateFunctionValue(double time) {
        double temperature = super.calculateFunctionValue(time);
        if (temperature < 0) {
            temperature = 0;
        }
        return temperature;
    }
}

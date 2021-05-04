package linearInterpolation.model;

// TODO: delete it or not
public class TemperatureFunctionInterpolation extends LinearInterpolation{
    @Override
    public void addPoint(double x) {
        if (getXInterpolated().contains(x)) {
            return;
        }
        getXInterpolated().add(x);
        double y = calculateFunctionValue(x);
        if (y < 0) {
            y = 0;
        }
        getYInterpolated().add(y);
        notifyObjectUpdateListeners();
    }
}

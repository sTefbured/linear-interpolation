package linearInterpolation.businessLogics;

public class Interpolator {
    private final double[] timeStamps;
    private final double[] temperatures;
    private double coefficientA;
    private double coefficientB;

    public Interpolator(double[] timeStamps, double[] temperatures) {
        this.timeStamps = timeStamps;
        this.temperatures = temperatures;
        initializeCoefficients();
    }

    private void initializeCoefficients() {
        double sumTimeStamps = 0;
        double sumTemperatures = 0;
        double sumTimeStampsSquared = 0;
        double sumXY = 0;
        for (int i = 0; i < timeStamps.length; i++) {
            sumTimeStamps += timeStamps[i];
            sumTemperatures += temperatures[i];
            sumTimeStampsSquared += timeStamps[i] * timeStamps[i];
            sumXY += timeStamps[i] * temperatures[i];
        }
        coefficientA = (sumXY * timeStamps.length - sumTimeStamps * sumTemperatures) / (timeStamps.length * sumTimeStampsSquared - sumTimeStamps * sumTimeStamps);
        coefficientB = (sumTemperatures - coefficientA * sumTimeStamps) / timeStamps.length;
    }

    public double calculateTemperature(double timeStamp) {
        return coefficientA * timeStamp + coefficientB;
    }
}

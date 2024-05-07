package ui.utilities;

public class Threshold {

    private final double lowerThreshold;
    private final double upperThreshold;

    public Threshold(double lowerThreshold, double upperThreshold) {
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
    }

    public double getLowerThreshold() {
        return lowerThreshold;
    }

    public double getUpperThreshold() {
        return upperThreshold;
    }
}


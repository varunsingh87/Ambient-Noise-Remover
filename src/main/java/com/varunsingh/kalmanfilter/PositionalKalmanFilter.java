package com.varunsingh.kalmanfilter;

public class PositionalKalmanFilter extends MeasureUpdatePredictFilter implements KalmanFilter {
    private double currentEstimateUncertainty;

    public final int MEASUREMENT_ERROR = 2;

    public PositionalKalmanFilter(int initialStateGuess, double initialEstimateUncertainty) {
        super(initialStateGuess);
        currentEstimateUncertainty = initialEstimateUncertainty;
    }
    
    public void measure(double measurement) {
        iteration++;
        currentMeasurement = measurement;
        currentState = calculateCurrentStateEstimate();
    }

    @Override
    public double calculateKalmanGain() {
        double currentEstimateUncertainty = calculateCurrentEstimateUncertainty();
        return currentEstimateUncertainty / (currentEstimateUncertainty + Math.pow(MEASUREMENT_ERROR, 2));
    }

    @Override
    public double calculateCurrentStateEstimate() {
        return calculatePrediction() + calculateKalmanGain() * (currentMeasurement - currentState);
    }

    @Override
    public double calculatePrediction() {
        return currentState;
    }

    @Override
    public double calculateCurrentEstimateUncertainty() {
        return (1 - calculateKalmanGain()) * currentEstimateUncertainty;
    }

    @Override
    public double calculateExtrapolatedEstimateUncertainty() {
        return currentEstimateUncertainty;
    }
    
}

package com.varunsingh.kalmanfilter;

public class PositionalKalmanFilter implements KalmanFilter {
    private double currentMeasurement;
    private double currentState;
    private double currentEstimateUncertainty;
    private int iteration = 0;

    public final int MEASUREMENT_ERROR = 2;

    public PositionalKalmanFilter(int initialStateGuess, double initialEstimateUncertainty) {
        currentState = initialStateGuess;
        currentEstimateUncertainty = initialEstimateUncertainty;
    }

    public int getIteration() {
        return iteration;
    }

    public double getCurrentMeasurement() {
        return currentMeasurement;
    }

    public double getCurrentState() {
        return currentState;
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

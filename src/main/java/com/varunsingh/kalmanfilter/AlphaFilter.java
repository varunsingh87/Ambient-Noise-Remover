package com.varunsingh.kalmanfilter;

public class AlphaFilter implements EstimationFilter {
    private double currentMeasurement;
    private double currentState;
    private int iteration = 0;

    public AlphaFilter(int initialStateGuess) {
        currentState = initialStateGuess;
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

    public double calculateAlphaFilter() {
        return iteration != 0.0 ? 1.0 / iteration : 1.0;
    }

    @Override
    public double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentState, 
            calculateAlphaFilter(), 
            currentMeasurement
        );
    }

    @Override
    public double calculatePrediction() {
        return currentState;
    }
}

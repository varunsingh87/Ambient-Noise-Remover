package com.varunsingh.kalmanfilter;

public class AlphaFilter extends MeasureUpdatePredictFilter implements EstimationFilter {

    public AlphaFilter(int initialStateGuess) {
        super(initialStateGuess);
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

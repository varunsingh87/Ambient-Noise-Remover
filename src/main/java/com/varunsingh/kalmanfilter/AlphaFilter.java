package com.varunsingh.kalmanfilter;

public class AlphaFilter extends MeasureUpdatePredictFilter {

    public AlphaFilter(int initialStateGuess) {
        super(initialStateGuess);
    }
    
    @Override
    public void measure(double measurement) {
        super.measure(measurement);
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
    public double calculateStateExtrapolation() {
        return currentState;
    }
}

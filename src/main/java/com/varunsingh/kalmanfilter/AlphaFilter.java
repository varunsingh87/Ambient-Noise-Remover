package com.varunsingh.kalmanfilter;

public class AlphaFilter extends MeasureUpdatePredictFilter<Double> {

    public AlphaFilter(double initialStateGuess) {
        super(initialStateGuess);
    }
    
    @Override
    public void measure(Double measurement) {
        super.measure(measurement);
        currentMeasurement = measurement;
        currentState = calculateCurrentStateEstimate();
    }

    public Double calculateAlphaFilter() {
        return iteration != 0.0 ? 1.0 / iteration : 1.0;
    }

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentState, 
            calculateAlphaFilter(), 
            currentMeasurement
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return currentState;
    }
}

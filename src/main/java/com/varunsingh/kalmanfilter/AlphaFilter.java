package com.varunsingh.kalmanfilter;

public class AlphaFilter implements EstimationFilter<Double>, Measurable<Double> {
    private SystemCycle cycleInfo;
    private int iteration = 0;

    public AlphaFilter(double initialStateGuess) {
        cycleInfo = new SystemCycle(initialStateGuess);
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
    }

    public SystemCycle getCycleInfo() {
        return cycleInfo;
    }

    public Double getCurrentStateEstimate() {
        return cycleInfo.getStateEstimate();
    }
    
    @Override
    public void measure(Double measurement) {
        iteration++;
        cycleInfo.setMeasurement(measurement);
        cycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
    }

    public Double calculateAlphaFilter() {
        return iteration != 0.0 ? 1.0 / iteration : 1.0;
    }

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            cycleInfo.getStateEstimate(), 
            calculateAlphaFilter(), 
            cycleInfo.getMeasurement()
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return getCurrentStateEstimate();
    }
}

package com.varunsingh.kalmanfilter;

public class PositionalKalmanFilter extends MeasureUpdatePredictFilter implements KalmanFilter {
    private double currentKalmanGain;
    private SystemState systemState;

    public PositionalKalmanFilter(int initialStateGuess, double measurementError, double estimateError) {
        super(initialStateGuess);

        systemState = new SystemState(initialStateGuess);
        systemState.setMeasurementUncertainty(Math.pow(measurementError, 2));
        systemState.setEstimateUncertainty(Math.pow(estimateError, 2));
        
        systemState.setStatePrediction(initialStateGuess);
        currentKalmanGain = calculateKalmanGain();
    }

    public SystemState getSystemState() {
        return systemState;
    }
    
    public void measure(double measurement) {
        super.measure(measurement);
        systemState.setMeasurement(measurement);
        currentKalmanGain = calculateKalmanGain();
        systemState.setStateEstimate(calculateCurrentStateEstimate());
        systemState.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
        systemState.setStatePrediction(calculateStateExtrapolation());
        systemState.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    @Override
    public double calculateKalmanGain() {
        return systemState.getEstimateUncertainty() / (systemState.getEstimateUncertainty() + systemState.getMeasurementUncertainty());
    }

    @Override
    public double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            systemState.getStatePrediction(), 
            currentKalmanGain, 
            systemState.getMeasurement()
        );
    }

    @Override
    public double calculateStateExtrapolation() {
        return systemState.getStateEstimate();
    }

    @Override
    public double calculateCurrentEstimateUncertainty() {
        return (1 - currentKalmanGain) * systemState.getEstimateUncertainty();
    }

    @Override
    public double calculateExtrapolatedEstimateUncertainty() {
        return systemState.getEstimateUncertainty();
    }
    
}

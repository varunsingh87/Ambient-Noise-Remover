package com.varunsingh.kalmanfilter;

public class OneDimensionalKalmanFilter extends MeasureUpdatePredictFilter implements KalmanFilter {
    private double currentKalmanGain;
    private double processNoise = 0;
    private SystemState systemState;

    public OneDimensionalKalmanFilter(int initialStateGuess, double measurementError, double estimateError) {
        super();

        systemState = new SystemState(initialStateGuess);
        systemState.setMeasurementUncertainty(Math.pow(measurementError, 2));
        systemState.setEstimateUncertainty(Math.pow(estimateError, 2));

        systemState.setStatePrediction(initialStateGuess);
        currentKalmanGain = calculateKalmanGain();
    }

    public OneDimensionalKalmanFilter(SystemState sysData, double pn) {
        super();

        systemState = sysData;
        setProcessNoise(pn);
        currentKalmanGain = calculateKalmanGain();
    }

    public SystemState getSystemState() {
        return systemState;
    }

    public double getProcessNoise() {
        return processNoise;
    }

    public void setProcessNoise(double processNoise) {
        this.processNoise = processNoise;
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
        return systemState.getEstimateUncertainty() + processNoise;
    }
    
}

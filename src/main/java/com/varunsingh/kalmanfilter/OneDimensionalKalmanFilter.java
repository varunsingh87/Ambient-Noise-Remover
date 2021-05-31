package com.varunsingh.kalmanfilter;

public class OneDimensionalKalmanFilter implements EstimationFilter<Double> {
    private double currentKalmanGain;
    private double processNoise = 0;
    private SystemCycle cycleInfo;
    private int iteration = 0;

    public OneDimensionalKalmanFilter(int initialStateGuess, double measurementError, double estimateError) {
        cycleInfo = new SystemCycle(initialStateGuess);
        cycleInfo.setMeasurementUncertainty(Math.pow(measurementError, 2));
        cycleInfo.setEstimateUncertainty(Math.pow(estimateError, 2));

        cycleInfo.setStatePrediction(initialStateGuess);
        currentKalmanGain = calculateKalmanGain();
    }

    public OneDimensionalKalmanFilter(SystemCycle sysData, double pn) {
        cycleInfo = sysData;
        setProcessNoise(pn);
        currentKalmanGain = calculateKalmanGain();
    }

    public SystemCycle getCycleInfo() {
        return cycleInfo;
    }

    public double getProcessNoise() {
        return processNoise;
    }

    public void setProcessNoise(double processNoise) {
        this.processNoise = processNoise;
    }

    public int getIteration() {
        return iteration;
    }
    
    public void measure(Double measurement) {
        iteration++;
        cycleInfo.setMeasurement(measurement);
    }
    
    @Override
    public void update() {
        currentKalmanGain = calculateKalmanGain();
        cycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        cycleInfo.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
    }

    @Override
    public void predict() {
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
        cycleInfo.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    public Double calculateKalmanGain() {
        return cycleInfo.getEstimateUncertainty() / (cycleInfo.getEstimateUncertainty() + cycleInfo.getMeasurementUncertainty());
    }

    private Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            cycleInfo.getStatePrediction(), 
            currentKalmanGain, 
            cycleInfo.getMeasurement()
        );
    }

    private double calculateStateExtrapolation() {
        return cycleInfo.getStateEstimate();
    }

    private double calculateCurrentEstimateUncertainty() {
        return (1 - currentKalmanGain) * cycleInfo.getEstimateUncertainty();
    }

    private double calculateExtrapolatedEstimateUncertainty() {
        return cycleInfo.getEstimateUncertainty() + processNoise;
    }
    
}

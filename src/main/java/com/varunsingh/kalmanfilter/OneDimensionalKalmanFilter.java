package com.varunsingh.kalmanfilter;

public class OneDimensionalKalmanFilter extends MeasureUpdatePredictFilter<Double> implements KalmanFilter<Double> {
    private double currentKalmanGain;
    private double processNoise = 0;
    private SystemCycle currentCycleInfo;

    public OneDimensionalKalmanFilter(int initialStateGuess, double measurementError, double estimateError) {
        super();

        currentCycleInfo = new SystemCycle(initialStateGuess);
        currentCycleInfo.setMeasurementUncertainty(Math.pow(measurementError, 2));
        currentCycleInfo.setEstimateUncertainty(Math.pow(estimateError, 2));

        currentCycleInfo.setStatePrediction(initialStateGuess);
        currentKalmanGain = calculateKalmanGain();
    }

    public OneDimensionalKalmanFilter(SystemCycle sysData, double pn) {
        super();

        currentCycleInfo = sysData;
        setProcessNoise(pn);
        currentKalmanGain = calculateKalmanGain();
    }

    public SystemCycle getSystemState() {
        return currentCycleInfo;
    }

    public double getProcessNoise() {
        return processNoise;
    }

    public void setProcessNoise(double processNoise) {
        this.processNoise = processNoise;
    }
    
    public void measure(Double measurement) {
        super.measure(measurement);
        currentCycleInfo.setMeasurement(measurement);
        currentKalmanGain = calculateKalmanGain();
        currentCycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        currentCycleInfo.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
        currentCycleInfo.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    @Override
    public Double calculateKalmanGain() {
        return currentCycleInfo.getEstimateUncertainty() / (currentCycleInfo.getEstimateUncertainty() + currentCycleInfo.getMeasurementUncertainty());
    }

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentCycleInfo.getStatePrediction(), 
            currentKalmanGain, 
            currentCycleInfo.getMeasurement()
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return currentCycleInfo.getStateEstimate();
    }

    @Override
    public Double calculateCurrentEstimateUncertainty() {
        return (1 - currentKalmanGain) * currentCycleInfo.getEstimateUncertainty();
    }

    @Override
    public Double calculateExtrapolatedEstimateUncertainty() {
        return currentCycleInfo.getEstimateUncertainty() + processNoise;
    }
    
}

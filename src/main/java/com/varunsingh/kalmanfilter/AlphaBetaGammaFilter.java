package com.varunsingh.kalmanfilter;

public class AlphaBetaGammaFilter implements EstimationFilter<Double> {
    private double alphaFilter;
    private double betaFilter;
    private double gammaFilter;
    private SystemCycle cycleInfo;
    private int iteration = 0;

    private static final double TIME_INTERVAL = 5;

    public AlphaBetaGammaFilter(SystemCycle initialState, double a, double b, double g) {
        cycleInfo = initialState;
        alphaFilter = a;
        betaFilter = b;
        gammaFilter = g;
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
        cycleInfo.setVelocityPrediction(calculateVelocityPrediction());
    }

    public SystemCycle getCycleInfo() {
        return cycleInfo;
    }

    public void setCycleInfo(SystemCycle newSystemCycle) {
        this.cycleInfo = newSystemCycle;
    }

    public double getAlphaFilter() {
        return alphaFilter;
    }

    public void setAlphaFilter(double alphaFilter) {
        this.alphaFilter = alphaFilter;
    }

    public double getBetaFilter() {
        return betaFilter;
    }

    public void setBetaFilter(double betaFilter) {
        this.betaFilter = betaFilter;
    }

    public double getGammaFilter() {
        return gammaFilter;
    }

    public void setGammaFilter(double gammaFilter) {
        this.gammaFilter = gammaFilter;
    }

    public int getIteration() {
        return iteration;
    }

    private double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            cycleInfo.getStatePrediction(),
            alphaFilter,
            cycleInfo.getMeasurement()
        );
    }

    public Double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            cycleInfo.getStatePrediction(), 
            cycleInfo.getVelocityPrediction(), 
            betaFilter, 
            cycleInfo.getMeasurement(), 
            TIME_INTERVAL
        );
    }

    private double calculateCurrentAcceleration() {
        return KalmanFilterEquations.useAccelerationStateUpdateEquation(
            cycleInfo.getStatePrediction(),
            cycleInfo.getStateAcceleration(),
            gammaFilter,
            cycleInfo.getMeasurement(),
            TIME_INTERVAL
        );
    }

    private double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            cycleInfo.getStateEstimate(), 
            TIME_INTERVAL, 
            cycleInfo.getStateVelocity(), 
            cycleInfo.getStateAcceleration()
        );
    }

    private double calculateVelocityPrediction() {
        return KalmanFilterEquations.useVelocityStateExtrapolationEquation(
            cycleInfo.getStateVelocity(),
            cycleInfo.getStateAcceleration(),
            TIME_INTERVAL
        );
    }

    @Override
    public void measure(Double measurement) {
        iteration++;
        cycleInfo.setMeasurement(measurement);
    }

    @Override
    public void update() {
        cycleInfo.setStateVelocity(calculateCurrentVelocity());
        cycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        cycleInfo.setStateAcceleration(calculateCurrentAcceleration());
    }

    @Override
    public void predict() {
        cycleInfo.setVelocityPrediction(calculateVelocityPrediction());
        cycleInfo.setStatePrediction(calculateStateExtrapolation());
    }
}

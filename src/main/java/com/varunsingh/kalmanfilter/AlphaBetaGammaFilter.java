package com.varunsingh.kalmanfilter;

public class AlphaBetaGammaFilter extends MeasureUpdatePredictFilter<Double> {
    private double alphaFilter;
    private double betaFilter;
    private double gammaFilter;
    private SystemCycle currentCycleInfo;

    private static final double TIME_INTERVAL = 5;

    public AlphaBetaGammaFilter(SystemCycle initialState, double a, double b, double g) {
        super(initialState.getStateEstimate());
        setSystemState(initialState);
        alphaFilter = a;
        betaFilter = b;
        gammaFilter = g;
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
        currentCycleInfo.setVelocityPrediction(calculateVelocityPrediction());
    }

    public SystemCycle getSystemState() {
        return currentCycleInfo;
    }

    public void setSystemState(SystemCycle systemState) {
        this.currentCycleInfo = systemState;
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

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentCycleInfo.getStatePrediction(),
            alphaFilter,
            currentCycleInfo.getMeasurement()
        );
    }

    public Double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            currentCycleInfo.getStatePrediction(), 
            currentCycleInfo.getVelocityPrediction(), 
            betaFilter, 
            currentCycleInfo.getMeasurement(), 
            TIME_INTERVAL
        );
    }

    public Double calculateCurrentAcceleration() {
        return KalmanFilterEquations.useAccelerationStateUpdateEquation(
            currentCycleInfo.getStatePrediction(),
            currentCycleInfo.getStateAcceleration(),
            gammaFilter,
            currentCycleInfo.getMeasurement(),
            TIME_INTERVAL
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            currentCycleInfo.getStateEstimate(), 
            TIME_INTERVAL, 
            currentCycleInfo.getStateVelocity(), 
            currentCycleInfo.getStateAcceleration()
        );
    }

    public Double calculateVelocityPrediction() {
        return KalmanFilterEquations.useVelocityStateExtrapolationEquation(
            currentCycleInfo.getStateVelocity(),
            currentCycleInfo.getStateAcceleration(),
            TIME_INTERVAL
        );
    }

    public void measure(Double measurement) {
        iteration++;
        currentCycleInfo.setMeasurement(measurement);
        currentCycleInfo.setStateVelocity(calculateCurrentVelocity());
        currentCycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        currentCycleInfo.setStateAcceleration(calculateCurrentAcceleration());
        currentCycleInfo.setVelocityPrediction(calculateVelocityPrediction());
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
    }
}

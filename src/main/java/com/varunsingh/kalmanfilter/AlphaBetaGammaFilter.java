package com.varunsingh.kalmanfilter;

public class AlphaBetaGammaFilter extends MeasureUpdatePredictFilter {
    private double alphaFilter;
    private double betaFilter;
    private double gammaFilter;
    private SystemState systemState;

    private static final double TIME_INTERVAL = 5;

    public AlphaBetaGammaFilter(SystemState initialState, double a, double b, double g) {
        super(initialState.getStateEstimate());
        setSystemState(initialState);
        alphaFilter = a;
        betaFilter = b;
        gammaFilter = g;
        systemState.setStatePrediction(calculateStateExtrapolation());
        systemState.setVelocityPrediction(calculateVelocityPrediction());
    }

    public SystemState getSystemState() {
        return systemState;
    }

    public void setSystemState(SystemState systemState) {
        this.systemState = systemState;
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
    public double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            systemState.getStatePrediction(),
            alphaFilter,
            systemState.getMeasurement()
        );
    }

    public double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            systemState.getStatePrediction(), 
            systemState.getVelocityPrediction(), 
            betaFilter, 
            systemState.getMeasurement(), 
            TIME_INTERVAL
        );
    }

    public double calculateCurrentAcceleration() {
        return KalmanFilterEquations.useAccelerationStateUpdateEquation(
            systemState.getStatePrediction(),
            systemState.getStateAcceleration(),
            gammaFilter,
            systemState.getMeasurement(),
            TIME_INTERVAL
        );
    }

    @Override
    public double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            systemState.getStateEstimate(), 
            TIME_INTERVAL, 
            systemState.getStateVelocity(), 
            systemState.getStateAcceleration()
        );
    }

    public double calculateVelocityPrediction() {
        return KalmanFilterEquations.useVelocityStateExtrapolationEquation(
            systemState.getStateVelocity(),
            systemState.getStateAcceleration(),
            TIME_INTERVAL
        );
    }

    public void measure(double measurement) {
        iteration++;
        systemState.setMeasurement(measurement);
        systemState.setStateVelocity(calculateCurrentVelocity());
        systemState.setStateEstimate(calculateCurrentStateEstimate());
        systemState.setStateAcceleration(calculateCurrentAcceleration());
        systemState.setVelocityPrediction(calculateVelocityPrediction());
        systemState.setStatePrediction(calculateStateExtrapolation());
    }
}

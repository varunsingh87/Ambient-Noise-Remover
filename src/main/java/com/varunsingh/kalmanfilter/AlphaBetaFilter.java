package com.varunsingh.kalmanfilter;

/**
 * Filter using the Measure, Update, Predict algorithm that estimates position
 * and velocity
 */
public class AlphaBetaFilter extends MeasureUpdatePredictFilter {
    private double currentVelocity;
    private double currentPrediction;
    private double alphaFilter;
    private double betaFilter;

    private final double TIME_INTERVAL = 5;

    /**
     * Constructor for AlphaBetaFilter
     * @param initialStateGuess The initial guess of the system state
     * @param initialVelocityGuess The initial guess of the velocity 
     * @param alphaFilter       The factor weight for the estimate of the position
     * @param betaFilter        The factor weight for the estimate of the velocity
     */
    public AlphaBetaFilter(int initialStateGuess, int initialVelocityGuess, double alpha, double beta) {
        super(initialStateGuess);
        currentVelocity = initialVelocityGuess;
        currentPrediction = calculateStateExtrapolation();
        setAlphaFilter(alpha);
        setBetaFilter(beta);
    }

    public double getBetaFilter() {
        return betaFilter;
    }

    public void setBetaFilter(double betaFilter) {
        this.betaFilter = betaFilter;
    }

    public double getAlphaFilter() {
        return alphaFilter;
    }

    public void setAlphaFilter(double alphaFilter) {
        this.alphaFilter = alphaFilter;
    }

    public double getCurrentVelocity() {
        return currentVelocity;
    }
    
    public double getCurrentPrediction() {
        return currentPrediction;
    }

    @Override
    public double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentPrediction, 
            alphaFilter, 
            currentMeasurement
        );
    }

    public double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            currentPrediction, 
            currentVelocity, 
            betaFilter, 
            currentMeasurement, 
            TIME_INTERVAL
        );
    }

    @Override
    public double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            currentState, 
            TIME_INTERVAL, 
            currentVelocity
        );
    }

    @Override
    public void measure(double measurement) {
        super.measure(measurement);
        currentVelocity = calculateCurrentVelocity();
        currentState = calculateCurrentStateEstimate();
        currentPrediction = calculateStateExtrapolation();
    }
    
}

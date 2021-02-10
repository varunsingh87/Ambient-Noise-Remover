package com.varunsingh.kalmanfilter;

/**
 * Filter using the Measure, Update, Predict algorithm that estimates position
 * and velocity
 */
public class AlphaBetaFilter extends MeasureUpdatePredictFilter<Double> {
    private Double currentVelocity;
    private Double currentPrediction;
    private Double alphaFilter;
    private Double betaFilter;

    private final int TIME_INTERVAL = 5;

    /**
     * Constructor for AlphaBetaFilter
     * @param initialStateGuess The initial guess of the system state
     * @param initialVelocityGuess The initial guess of the velocity 
     * @param alphaFilter       The factor weight for the estimate of the position
     * @param betaFilter        The factor weight for the estimate of the velocity
     */
    public AlphaBetaFilter(double initialStateGuess, double initialVelocityGuess, Double alpha, Double beta) {
        super(initialStateGuess);
        currentVelocity = initialVelocityGuess;
        currentPrediction = calculateStateExtrapolation();
        setAlphaFilter(alpha);
        setBetaFilter(beta);
    }

    public Double getBetaFilter() {
        return betaFilter;
    }

    public void setBetaFilter(Double betaFilter) {
        this.betaFilter = betaFilter;
    }

    public Double getAlphaFilter() {
        return alphaFilter;
    }

    public void setAlphaFilter(Double alphaFilter) {
        this.alphaFilter = alphaFilter;
    }

    public Double getCurrentVelocity() {
        return currentVelocity;
    }
    
    public Double getCurrentPrediction() {
        return currentPrediction;
    }

    @Override
    public Double calculateCurrentStateEstimate() {
        return KalmanFilterEquations.usePositionalStateUpdateEquation(
            currentPrediction, 
            alphaFilter, 
            currentMeasurement
        );
    }

    public Double calculateCurrentVelocity() {
        return KalmanFilterEquations.useVelocityStateUpdateEquation(
            currentPrediction, 
            currentVelocity, 
            betaFilter, 
            currentMeasurement, 
            TIME_INTERVAL
        );
    }

    @Override
    public Double calculateStateExtrapolation() {
        return KalmanFilterEquations.usePositionalStateExtrapolationEquation(
            currentState, 
            TIME_INTERVAL, 
            currentVelocity
        );
    }

    @Override
    public void measure(Double measurement) {
        super.measure(measurement);
        currentVelocity = calculateCurrentVelocity();
        currentState = calculateCurrentStateEstimate();
        currentPrediction = calculateStateExtrapolation();
    }
    
}

package com.varunsingh.kalmanfilter;

public class KalmanFilterEquations {
    public static double usePositionalStateUpdateEquation(double previousEstimate, double kalmanGain, double measurement) {
        return previousEstimate + kalmanGain * (measurement - previousEstimate);
    }

    public static double useVelocityStateUpdateEquation(double previousStateEstimate, double previousVelocityEstimate, double betaFilter, double measurement, double timeInterval) {
        return previousVelocityEstimate + betaFilter * ((measurement - previousStateEstimate) / timeInterval);
    }

    public static double usePositionalStateExtrapolationEquation(double currentEstimate, double timeInterval, double currentVelocity) {
        return currentEstimate + timeInterval * currentVelocity;
    }
}

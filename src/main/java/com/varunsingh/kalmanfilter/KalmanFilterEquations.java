package com.varunsingh.kalmanfilter;

public class KalmanFilterEquations {
    public static double usePositionalStateUpdateEquation(double previousEstimate, double kalmanGain,
            double measurement) {
        return previousEstimate + kalmanGain * (measurement - previousEstimate);
    }

    public static double useVelocityStateUpdateEquation(double previousStateEstimate, double previousVelocityEstimate,
            double betaFilter, double measurement, double timeInterval) {
        return previousVelocityEstimate + betaFilter * ((measurement - previousStateEstimate) / timeInterval);
    }

    public static double useAccelerationStateUpdateEquation(double previousStateEstimate,
            double previousAccelerationEstimate, double gammaFilter, double measurement, double timeInterval) {
        return previousAccelerationEstimate
                + gammaFilter * ((measurement - previousStateEstimate) / (0.5 * Math.pow(timeInterval, 2)));
    }

    public static double usePositionalStateExtrapolationEquation(double currentEstimate, double timeInterval,
            double currentVelocity) {
        return currentEstimate + timeInterval * currentVelocity;
    }

    public static double usePositionalStateExtrapolationEquation(double currentEstimate, double timeInterval, double currentVelocity, double currentAcceleration) {
        return usePositionalStateExtrapolationEquation(currentEstimate, timeInterval, currentVelocity) + currentAcceleration * (Math.pow(timeInterval, 2) / 2);
    }

	public static double useVelocityStateExtrapolationEquation(double currentVelocity, double currentAcceleration, double timeInterval) {
		return currentVelocity + currentAcceleration * timeInterval;
	}
}

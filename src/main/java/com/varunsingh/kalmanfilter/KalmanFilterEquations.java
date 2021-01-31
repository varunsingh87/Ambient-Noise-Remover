package com.varunsingh.kalmanfilter;

public class KalmanFilterEquations {
    public static double usePositionalStateUpdateEquation(double previousEstimate, double kalmanGain, double measurement) {
        return previousEstimate + kalmanGain * (measurement - previousEstimate);
    }
}

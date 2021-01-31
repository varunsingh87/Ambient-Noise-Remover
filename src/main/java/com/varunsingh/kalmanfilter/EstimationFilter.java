package com.varunsingh.kalmanfilter;

public interface EstimationFilter {
    double calculateCurrentStateEstimate();

    double calculatePrediction();

    void measure(double measurement);
}

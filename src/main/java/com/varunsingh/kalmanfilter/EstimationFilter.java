package com.varunsingh.kalmanfilter;

public interface EstimationFilter {
    double calculateCurrentStateEstimate();

    double calculateStatePrediction();

    void measure(double measurement);
}

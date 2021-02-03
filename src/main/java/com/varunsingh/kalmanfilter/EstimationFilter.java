package com.varunsingh.kalmanfilter;

public interface EstimationFilter {
    double calculateCurrentStateEstimate();

    double calculateStateExtrapolation();

    void measure(double measurement);
}

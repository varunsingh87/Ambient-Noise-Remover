package com.varunsingh.kalmanfilter;

public interface KalmanFilter extends EstimationFilter {
    double calculateKalmanGain();

    double calculateCurrentEstimateUncertainty();

    double calculateExtrapolatedEstimateUncertainty();
}

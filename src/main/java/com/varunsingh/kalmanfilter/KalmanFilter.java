package com.varunsingh.kalmanfilter;

public interface KalmanFilter<T> extends EstimationFilter<T> {
    T calculateKalmanGain();

    T calculateCurrentEstimateUncertainty();

    T calculateExtrapolatedEstimateUncertainty();
}

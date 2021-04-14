package com.varunsingh.kalmanfilter;

public interface EstimationFilter<T> {
    T calculateCurrentStateEstimate();

    T calculateStateExtrapolation();
}

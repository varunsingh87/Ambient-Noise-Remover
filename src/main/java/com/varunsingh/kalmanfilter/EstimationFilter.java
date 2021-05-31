package com.varunsingh.kalmanfilter;

public interface EstimationFilter<T> {
    default void runAlgorithm(T measurement) {
        measure(measurement);
        update();
        predict();
    }

    void measure(T measurement);

    void update();

    void predict();
}

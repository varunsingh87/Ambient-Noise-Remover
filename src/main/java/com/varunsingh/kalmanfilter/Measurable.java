package com.varunsingh.kalmanfilter;

public interface Measurable<T> {
    void measure(T measurement);
}

package com.varunsingh.kalmanfilter;

abstract class MeasureUpdatePredictFilter implements EstimationFilter {
    protected double currentMeasurement;
    protected double currentState;
    protected int iteration = 0;

    MeasureUpdatePredictFilter(int initialStateGuess) {
        currentState = initialStateGuess;
    }

    public double getCurrentMeasurement() {
        return currentMeasurement;
    }
    
    public double getCurrentState() {
        return currentState;
    }
    
    public int getIteration() {
        return iteration;
    }

    public void measure(double measurement) {
        iteration++;
        currentMeasurement = measurement;
    }
}

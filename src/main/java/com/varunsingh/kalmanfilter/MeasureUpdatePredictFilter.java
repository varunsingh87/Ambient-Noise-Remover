package com.varunsingh.kalmanfilter;

abstract class MeasureUpdatePredictFilter<T> implements EstimationFilter<T> {
    protected T currentMeasurement; // TODO Remove
    protected T currentState; // TODO Remove
    
    protected int iteration = 0;
    protected SystemCycle currentCycleInfo;

    MeasureUpdatePredictFilter() {
        
    }

    MeasureUpdatePredictFilter(T initialStateGuess) {
        // TODO Remove
        currentState = initialStateGuess;
    }

    public T getCurrentMeasurement() {
        return currentMeasurement;
    }
    
    public T getCurrentState() {
        return currentState;
    }
    
    public int getIteration() {
        return iteration;
    }

    public SystemCycle getCurrentCycleInfo() {
        return currentCycleInfo;
    }

    public void setCurrentCycleInfo(SystemCycle newCycleInfo) {
        currentCycleInfo = newCycleInfo;
    }

    public void measure(T measurement) {
        iteration++;
        currentMeasurement = measurement;
    }
}

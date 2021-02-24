package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class SystemCycleVector {
    private Vector measurement;
    private Vector stateEstimate;
    private Vector statePrediction;
    private Matrix measurementUncertainty;
    private Vector estimateUncertainty;
    private Vector estimateUncertaintyPrediction;

    SystemCycleVector(Vector initialGuess, Vector estimateUncertainty) {
        setStateEstimate(initialGuess);
        setEstimateUncertainty(estimateUncertainty);
        setStatePrediction(initialGuess);
        setEstimateUncertaintyPrediction(estimateUncertainty);
    }

    public Vector getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Vector measurement) {
        this.measurement = measurement;
    }

    public Vector getStateEstimate() {
        return stateEstimate;
    }

    public void setStateEstimate(Vector stateEstimate) {
        this.stateEstimate = stateEstimate;
    }
    
    public Vector getStatePrediction() {
        return statePrediction;
    }

    public void setStatePrediction(Vector statePrediction) {
        this.statePrediction = statePrediction;
    }

    public Matrix getMeasurementUncertainty() {
        return measurementUncertainty;
    }

    public void setMeasurementUncertainty(Matrix measurementUncertainty) {
        this.measurementUncertainty = measurementUncertainty;
    }

    public Matrix getEstimateUncertainty() {
        return estimateUncertainty;
    }

    public void setEstimateUncertainty(Vector estimateUncertainty) {
        this.estimateUncertainty = estimateUncertainty;
    }

    public Vector getEstimateUncertaintyPrediction() {
        return estimateUncertaintyPrediction;
    }

    public void setEstimateUncertaintyPrediction(Vector estimateUncertaintyPrediction) {
        this.estimateUncertaintyPrediction = estimateUncertaintyPrediction;
    }
}

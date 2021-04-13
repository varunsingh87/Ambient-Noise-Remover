package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class SystemCycleVector {
    private Vector measurement;
    private Vector stateEstimate;
    private Vector statePrediction;
    private Matrix measurementUncertainty;
    private Matrix estimateUncertainty;
    private Matrix estimateUncertaintyPrediction;
    
    public SystemCycleVector(Vector initialGuess, Matrix estimateUncertainty) {
        setStateEstimate(initialGuess);
        setEstimateUncertainty(estimateUncertainty);
        setStatePrediction(initialGuess);
        setEstimateUncertaintyPrediction(estimateUncertainty);
    }

    public boolean checkAllParametersAreInitialized() {
        return stateEstimate != null 
            && statePrediction != null 
            && measurementUncertainty != null 
            && estimateUncertainty != null;
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

    public void setEstimateUncertainty(Matrix estimateUncertainty) {
        this.estimateUncertainty = estimateUncertainty;
    }

    public Matrix getEstimateUncertaintyPrediction() {
        return estimateUncertaintyPrediction;
    }

    public void setEstimateUncertaintyPrediction(Matrix estimateUncertaintyPrediction) {
        this.estimateUncertaintyPrediction = estimateUncertaintyPrediction;
    }
}

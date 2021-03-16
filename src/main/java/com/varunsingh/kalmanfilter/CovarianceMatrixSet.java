package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;

public class CovarianceMatrixSet {
    private Matrix stateTransition;
    private Matrix control;
    private Matrix observation;

    public CovarianceMatrixSet() {}

    public Matrix getObservation() {
        return observation;
    }

    public void setObservation(Matrix observation) {
        this.observation = observation;
    }

    public Matrix getControl() {
        return control;
    }

    public void setControl(Matrix control) {
        this.control = control;
    }

    public Matrix getStateTransition() {
        return stateTransition;
    }

    public void setStateTransition(Matrix stateTransition) {
        this.stateTransition = stateTransition;
    }
}

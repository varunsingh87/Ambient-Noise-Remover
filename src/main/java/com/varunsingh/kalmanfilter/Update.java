package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class Update implements Updateable {
    Matrix observation;

    Vector stateVector;
    Vector noise;

    public Update() {
        observation = calculateObservabilityOfMeasurementModel();
    }

    @Override
    public Vector mapStateVectorToMeasurementVector() {
        return observation
            .times(stateVector)
            .plus(noise)
            .asColumnVector();
    }

    @Override
    public Matrix calculateObservabilityOfMeasurementModel() {
        return new Matrix(new double[][] {
            { 1, 0 },
            { 0, 1 }
        });
    }

    @Override
    public Matrix calculateObservabilityOfMeasurementCovariance() {
        return null;
    }

}

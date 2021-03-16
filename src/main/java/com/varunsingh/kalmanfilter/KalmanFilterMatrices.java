package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;

public class KalmanFilterMatrices {
    static Matrix getStateTransitionMatrix(double timeInterval) {
        return new Matrix(new double[][] { { 1, timeInterval, 0 }, { 0, 1, timeInterval }, { 0, 0, 1, }, });
    }

    static Matrix getControlMatrix(double timeInterval) {
        return Matrix.createIdentityMatrix(3);
    }

    static Matrix createObservationMatrix(int measurementVectorSize, int stateVectorSize) {
        Matrix observationMatrix = new Matrix(new double[][] {
            { 0.004, 0.005, 0.003 },
            { 0.017, 0.324, 0.145 },
            { 0.364, 0.879, 0.128 }, 
            { 0.356, 0.112, 0.040 }
        });

        return observationMatrix;
    }
}

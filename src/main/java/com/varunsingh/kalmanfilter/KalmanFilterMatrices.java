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
        Matrix observationMatrix = new Matrix(new double[measurementVectorSize][stateVectorSize]);
        
        for (int i = 0; i < measurementVectorSize; i++) {
            for (int j = 0; j < stateVectorSize; j++) {
                observationMatrix.set(i, j, Math.abs(Math.random() * 2));
            }
        }

        return observationMatrix;
    }
}

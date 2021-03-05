package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;

public class KalmanFilterMatrices {
    static Matrix getStateTransitionMatrix(double timeInterval) {
        return new Matrix(new double[][] {
            { 1, timeInterval, 0            },
            { 0, 1,            timeInterval },
            { 0, 0,            1,           },
        });
    }

    static Matrix getControlMatrix(double timeInterval) {
        return Matrix.createIdentityMatrix(3);
    }

    static Matrix getObservationMatrix(int numRows, int numCols) {
        return new Matrix(new double[][] {
            { 1, 0, 0 },
            { 0, 1, 0 },
            { 0, 0, 1 }
        });
    }
}

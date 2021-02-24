package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;

public class KalmanFilterMatrices {
    static Matrix getStateTransitionMatrix(double timeInterval) {
        return new Matrix(new double[][] {
            { 1, 0, 0, timeInterval, 0,            0            },
            { 0, 1, 0, 0,            timeInterval, 0            },
            { 0, 0, 1, 0,            0,            timeInterval },
            { 0, 0, 0, 1,            0,            0            },
            { 0, 0, 0, 0,            1,            0            },
            { 0, 0, 0, 0,            0,            1            }
        });
    }

    static Matrix getControlMatrix(double timeInterval) {
        double accelerationProduct = 0.5 * Math.pow(timeInterval, 2);
        return new Matrix(new double[][] {
            { accelerationProduct, 0,                   0                   },
            { 0,                   accelerationProduct, 0                   },
            { 0,                   0,                   accelerationProduct },
            { timeInterval,        0,                   0                   },
            { 0,                   timeInterval,        0                   },
            { 0,                   0,                   timeInterval}
        });
    }

    static Matrix getObservationMatrix(int numRows, int numCols) {
        return new Matrix(new double[][] {
            { 1, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 1 }
        });
    }

    public static Matrix getIdentityMatrix(int numDimens) {
        double[][] identityElements = new double[numDimens][numDimens];

        for (int i = 0; i < numDimens; i++) {
            for (int j = 0; j < numDimens; j++) {
                identityElements[i][j] = i == j ? 1 : 0;
            }
        }
        
        return new Matrix(identityElements);
    }
}

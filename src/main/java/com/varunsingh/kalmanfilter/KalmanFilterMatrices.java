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
}

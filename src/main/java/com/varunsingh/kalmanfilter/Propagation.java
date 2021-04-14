package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class Propagation implements Propagatable {
    private Vector previousStateVector;
    private Matrix previousEstimateUncertainty;
    private Matrix processNoise;

    /**
     * The state transition matrix is the mathematical model
     * that defines how the state vector changes over time
     */
    private Matrix stateTransitionMatrix;

    @Override
    public Vector predictNextStateVector() {
        return new Vector(
            new double[] {
                previousStateVector.get(0) + previousStateVector.get(1) * MultiDimensionalKalmanFilter.TIME_INTERVAL,
                previousStateVector.get(1)
            }
        );
    }

    @Override
    public Vector modelNextStateVectorWithNoise() {
        return stateTransitionMatrix.times(previousStateVector).asRowVector();
    }

    @Override
    public Matrix modelNextEstimateUncertaintyWithNoise() {
        Matrix stateCovarianceMatrix = stateTransitionMatrix
            .times(previousEstimateUncertainty)
            .times(stateTransitionMatrix);
        
        Matrix estimateUncertaintyWithProcessNoise = stateCovarianceMatrix.plus(processNoise);

        return estimateUncertaintyWithProcessNoise;
    }

}

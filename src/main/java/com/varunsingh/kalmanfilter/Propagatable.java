package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

/**
 * During the propagate step, a mathematical model is applied over a specified period of time 
 * to predict the next state vector and state covariance matrix of the system. 
 * This mathematical model is known as the state transition matrix, Φ or F, 
 * and is used to define how the state vector changes over time.
 * The model must also include noise to account for disturbances and errors in the state prediction.
 * Therefore, the propagate step also includes adjustments by the process noise covariance matrix, Q
 * to define uncertainty in the noise
 * @link https://www.vectornav.com/resources/kalman-filters (Propagate Step section)
 */
interface Propagatable {
    Vector predictNextStateVector();

    // Matrix predictNextEstimateUncertainty();

    Matrix modelNextStateVectorWithNoise();

    Matrix modelNextEstimateUncertaintyWithNoise();
}

package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

/**
 * The update step follows the propagation step and combines the prediction of
 * the state vector and state covariance matrix with any available measurement
 * data from the suite of sensors on board a system to provide an optimal
 * estimate of these parameters. The Kalman filter assumes these measurements
 * are valid at an instantaneous time of validity, so timing is critical.
 */
interface Updateable {
    Vector mapStateVectorToMeasurementVector();

    Matrix calculateObservabilityOfMeasurementModel();

    Matrix calculateObservabilityOfMeasurementCovariance();
}

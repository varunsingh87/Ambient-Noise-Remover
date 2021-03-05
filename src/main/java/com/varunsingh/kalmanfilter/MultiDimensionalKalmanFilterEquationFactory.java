package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.MatrixCovarianceOperation;
import com.varunsingh.linearalgebra.Vector;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;

public class MultiDimensionalKalmanFilterEquationFactory {
    private double timeInterval;

    public MultiDimensionalKalmanFilterEquationFactory(double t) {
        timeInterval = t;
    }

    Vector useStateExtrapolationEquation(Vector v, Vector v2) {
        Matrix firstSum = KalmanFilterMatrices.getControlMatrix(timeInterval).times(v);
        Matrix secondSum = KalmanFilterMatrices.getStateTransitionMatrix(timeInterval).times(v2);
        return Vector.valueOf(firstSum.plus(secondSum));
    }

    Matrix useCovarianceExtrapolationEquation(Matrix currentEstUnc, Matrix processNoiseUncertainty) {
        return currentEstUnc.times(KalmanFilterMatrices.getStateTransitionMatrix(timeInterval))
                .plus(processNoiseUncertainty);
    }

    Matrix useMeasurementEquation(Matrix observation, Vector hiddenState, Vector randomNoise) {
        return observation.times(hiddenState).plus(randomNoise);
    }

    /**
     * Calculates the measurement uncertainty
     * 
     * @param measurementError measurement error
     * @return Rn The covariance matrix of the measurement
     */
    Matrix useMeasurementUncertaintyEquation(Vector measurementError) {
        return new MatrixCovarianceOperation(measurementError.transpose(), measurementError).compute();
    }

    /**
     * Calculates the process noise uncertainty
     * 
     * @param processNoise the process noise vector
     * @return A covariance matrix of the process noise
     */
    Matrix useProcessNoiseUncertaintyEquation(Vector processNoise) {
        return new MatrixCovarianceOperation(processNoise.transpose(), processNoise).compute();
    }

    Vector useStateUpdateEquation(Vector previousState, Matrix kalmanGain, Vector measurement) {
        Matrix innovation = measurement.minus(
            KalmanFilterMatrices.getObservationMatrix(3, 3).times(previousState));

        return (Vector) previousState.plus(kalmanGain.times(innovation));
    }

    Matrix useCovarianceUpdateEquation(Matrix kalmanGain, Matrix observation, Matrix prevEstUnc,
            Matrix measurementUnc) {
        // I - Kn*H
        Matrix identityKalmanObserve = Matrix.createIdentityMatrix(kalmanGain.getColumns())
                .minus(kalmanGain
                    .times(
                        KalmanFilterMatrices.getObservationMatrix(
                            measurementUnc.getRows(), 
                            measurementUnc.getColumns()
                        )
                    )
                );

        // Kn*Rn*KnT
        Matrix lastTerm = kalmanGain.times(measurementUnc).times(kalmanGain.transpose());

        return identityKalmanObserve.times(prevEstUnc).times(identityKalmanObserve.transpose()).plus(lastTerm);
    }

    Matrix useKalmanGainEquation(Matrix prevEstUnc, Matrix observation, Matrix measurementUnc) throws MatrixNotInvertibleException {
        Matrix nonCurrMatrixProd = prevEstUnc
            .times(observation.transpose());
        Matrix uncertaintiesObservationInverse = observation
            .times(nonCurrMatrixProd)
            .plus(measurementUnc)
            .invert();
        return nonCurrMatrixProd
            .times(uncertaintiesObservationInverse);
    }
}

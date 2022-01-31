package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;
import com.varunsingh.linearalgebra.Vector;

/**
 * Step 1: Predict state
 * Step 2: Initial Process Covariance
 * Step 3: Predicted
 * Process Covariance
 * Step 4: Calculating the Kalman Gain
 * Step 5: The New
 * Observation (Update Measurement)
 * Step 6: Calculating the Current State
 * Step 7: Updating the Process Covariance
 * Step 8: Change to next iteration
 */
public class KalmanFilter {
	/**
	 * Error in the measurement/observation Each element corresponds to the
	 * error in the corresponding position in X, the state vector
	 */
	private Vector observationError;

	private Calculations calculations;
	private int iteration;

	/**
	 * The change in time between consecutive iterations
	 */
	private double timeOfOneCycle = 1.0;

	/**
	 * Constructor for the Kalman Filter
	 *
	 * @param initEst     The initial estimate of the system
	 * @param obErr       The error in the observation/measurement
	 * @param initProcErr The error in the process/estimate
	 */
	KalmanFilter(Vector initEst, Vector obErr, Vector initProcErr) {
		observationError = obErr;
		calculations = new Calculations(initEst, initProcErr);
	}

	/**
	 * @return the observationError
	 */
	public Vector getObservationError() {
		return observationError;
	}

	public Matrix getMeasurementCovariance() {
		return new Matrix(
			new double[][] { { Math.pow(observationError.get(0), 2), 0 }, { 0,
				Math.pow(observationError.get(1), 2) } }
		);
	}

	public Calculations getCurrentSystemValues() {
		return calculations;
	}

	public Calculations initialize(Vector initialEstimate, Vector initialErrorEstimate) {
		throw new UnsupportedOperationException(
			"Initialization is not implemented yet"
		);
	}

	private Matrix stateTransition() {
		return new Matrix(new double[][] { { 1, timeOfOneCycle }, { 0, 1 } });
	}

	/**
	 * Step 1 of the Kalman Filter
	 *
	 * @return
	 */
	Dataset predictState() {
		Vector previousState = calculations.estimate();

		Vector adaptiveControl = Vector.column(
			new double[] { 0.5 * Math.pow(timeOfOneCycle, 2), timeOfOneCycle }
		);

		Matrix controlVariable = new Matrix(2);

		return stateTransition().times(previousState).plus(
			adaptiveControl.times(controlVariable)
		);
	}

	Dataset initialProcessCovariance() {
		Matrix processCovariance = new Matrix(2, 2);

		double firstVariableProcessError = calculations.processError().get(0);
		double secondVariableProcessError = calculations.processError().get(1);

		processCovariance.set(0, 0, Math.pow(firstVariableProcessError, 2));
		processCovariance.set(
			0, 1, firstVariableProcessError * secondVariableProcessError
		);
		processCovariance.set(
			1, 0, firstVariableProcessError * secondVariableProcessError
		);
		processCovariance.set(1, 1, Math.pow(secondVariableProcessError, 2));

		return processCovariance.zeroOutMinorDiagonal();
	}

	Dataset predictProcessCovariance(Matrix initialProcessCovariance, Matrix noise) {
		Matrix result = (Matrix) stateTransition().times(
			initialProcessCovariance
		).times(stateTransition().transpose()).plus(noise);

		return result.zeroOutMinorDiagonal();
	}

	Dataset predictProcessCovariance(Matrix initialProcessCovariance) {
		Matrix result = (Matrix) stateTransition().times(
			initialProcessCovariance
		).times(stateTransition().transpose());

		return result.zeroOutMinorDiagonal();
	}

	Dataset calculateKalmanGain(Matrix processCovariancePrediction) {
		Matrix observationMatrix = Matrix.createIdentityMatrix(2);

		Matrix numerator = (Matrix) processCovariancePrediction.times(
			observationMatrix.transpose()
		);

		Matrix denominator = (Matrix) observationMatrix.times(numerator).plus(
			getMeasurementCovariance()
		);

		try {
			return numerator.divide(denominator);
		} catch (MatrixNotInvertibleException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Step 5 of the Kalman Filter
	 *
	 * @description Calculates measurement of the state
	 * @returnc     Measurement of the state
	 */
	Dataset calculateNewObservation(Vector measuredObservation, Vector noise) {
		// TODO (Primary) Reflect size of state vector
		// TODO (Secondary) Change this to reflect which variables in the state
		// vector are getting observed
		Matrix observation = Matrix.createIdentityMatrix(2);

		return (Dataset) observation.times(measuredObservation).plus(noise);
	}

	Dataset calculateNewObservation(Vector measuredObservation) {
		return Matrix.createIdentityMatrix(2).times(measuredObservation);
	}

	Dataset updateState(Vector predictedState, Vector measurementState, Matrix kalmanGain) {
		Matrix observation = Matrix.createIdentityMatrix(2);

		Dataset adaptedPrediction = observation.times(predictedState);

		return predictedState.plus(
			kalmanGain.times(measurementState.minus(adaptedPrediction))
		);
	}

	Dataset updateProcesCovariance(Matrix predictedProcessCovariance, Matrix kalmanGain) {
		Dataset identity = Matrix.createIdentityMatrix(2);
		Dataset observation = Matrix.createIdentityMatrix(2);

		return identity.minus(kalmanGain.times(observation)).times(
			predictedProcessCovariance
		);
	}
}

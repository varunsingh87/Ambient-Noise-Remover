package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;
import com.varunsingh.linearalgebra.Vector;

public class KalmanFilter {
	/**
	 * Error in the measurement/observation Each element corresponds to the
	 * error in the corresponding position in X, the state vector
	 */
	private Vector observationError;

	private Matrix controlVariable;

	/**
	 * The change in time between consecutive iterations
	 */
	private double timeOfOneCycle = 1.0;

	/**
	 * Constructor for the Kalman Filter
	 * 
	 * @param obErr The error in the observation/measurement
	 */
	public KalmanFilter(Vector obErr, Matrix u) {
		observationError = obErr;
		controlVariable = u;
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

	private Matrix stateTransition() {
		return new Matrix(new double[][] { { 1, timeOfOneCycle }, { 0, 1 } });
	}

	/**
	 * Step 1 of the Kalman Filter
	 * 
	 * @param  previousState The previous state of the system
	 * @return               The state prediction
	 */
	Dataset predictState(Vector previousState) {
		Vector adaptiveControl = Vector.column(
			new double[] { 0.5 * Math.pow(timeOfOneCycle, 2), timeOfOneCycle }
		);

		return stateTransition().times(previousState).plus(
			adaptiveControl.times(controlVariable)
		);
	}

	public Matrix initialProcessCovariance(Vector processError) {
		Matrix processCovariance = new Matrix(2, 2);

		double firstVariableProcessError = processError.get(0);
		double secondVariableProcessError = processError.get(1);

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
		Matrix observationMatrix = Matrix.createIdentityMatrix(
			processCovariancePrediction.getRows()
		);

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
	 * @return      Measurement of the state
	 */
	Dataset calculateNewObservation(Vector measuredObservation, Vector noise) {
		// TODO (Secondary) Change this to reflect which variables in the state
		// vector are getting observed
		Matrix observation = Matrix.createIdentityMatrix(
			measuredObservation.getSize()
		);

		return (Dataset) observation.times(measuredObservation).plus(noise);
	}

	Dataset calculateNewObservation(Vector measuredObservation) {
		return Matrix.createIdentityMatrix(measuredObservation.getSize()).times(
			measuredObservation
		);
	}

	Dataset updateState(Vector predictedState, Vector measurementState, Matrix kalmanGain) {
		Matrix observation = Matrix.createIdentityMatrix(
			measurementState.getSize()
		);

		Dataset adaptedPrediction = observation.times(predictedState);

		return predictedState.plus(
			kalmanGain.times(measurementState.minus(adaptedPrediction))
		);
	}

	Dataset updateProcessCovariance(Matrix predictedProcessCovariance, Matrix kalmanGain) {
		Dataset identity = Matrix.createIdentityMatrix(kalmanGain.getRows());
		Dataset observation = Matrix.createIdentityMatrix(
			kalmanGain.getColumns()
		);

		return identity.minus(kalmanGain.times(observation)).times(
			predictedProcessCovariance
		);
	}

	/**
	 * Step 1: Predict state
	 * Step 2: Initial Process Covariance
	 * Step 3: Predicted Process Covariance
	 * Step 4: Calculating the Kalman Gain
	 * Step 5: The New Observation (Update Measurement)
	 * Step 6: Calculating the Current State
	 * Step 7: Updating the Process Covariance
	 * 
	 * @param  previousCalculation The previous calculation (state, process)
	 * @param  measuredObservation The measurement of the state
	 * @return                     Calculation with updated state and process
	 *                             covariance
	 */
	public Calculations execute(Calculations previousCalculations, Vector measuredObservation) {
		Vector predictedState = (Vector) predictState(
			previousCalculations.stateEstimate()
		);

		Matrix predictedProcessCovariance = (Matrix) predictProcessCovariance(
			previousCalculations.processCovariance()
		);

		Matrix kalmanGain = (Matrix) calculateKalmanGain(
			predictedProcessCovariance
		);
		Vector newObservation = (Vector) calculateNewObservation(
			measuredObservation
		);

		Vector updatedState = (Vector) updateState(
			predictedState, newObservation, kalmanGain
		);

		Matrix updatedProcessCovariance = (Matrix) updateProcessCovariance(
			predictedProcessCovariance, kalmanGain
		);

		return new Calculations(updatedState, updatedProcessCovariance);
	}
}

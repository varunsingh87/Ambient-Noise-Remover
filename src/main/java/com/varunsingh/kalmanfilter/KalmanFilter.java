package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;
import com.varunsingh.linearalgebra.Vector;

/**
 * Step 1: Predict state
 * Step 2: Initial Process Covariance
 * Step 3: Predicted Process Covariance
 * Step 4: Calculating the Kalman Gain
 * Step 5: The New Observation (Update Measurement)
 * Step 6: Calculating the Current State
 * Step 7: 
 */
public class KalmanFilter {
    /**
     * Error in the measurement/observation
     * Each element corresponds to the error in the corresponding position in X, the state vector
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
     * @param initEst The initial estimate of the system
     * @param obErr The error in the observation/measurement
     * @param initProcErr The error in the process/estimate
     */
    KalmanFilter(Vector initEst, Vector obErr, Vector initProcErr) {
        observationError = obErr;
        calculations = new Calculations(0.0, initEst, initProcErr);
    }

    /**
     * @return the observationError
     */
    public Vector getObservationError() {
        return observationError;
    }

    public Matrix getMeasurementCovariance() {
        return new Matrix(new double[][] {
            { Math.pow(observationError.get(0), 2), 0 },
            { 0, Math.pow(observationError.get(1), 2) }
        });
    }

    public Calculations getCurrentSystemValues() {
        return calculations;
    }
    
    public Calculations initialize(Vector initialEstimate, Vector initialErrorEstimate) {
        throw new UnsupportedOperationException("Initialization is not implemented yet");
    }

    void executeKalmanFilter(double measurement) {
        calculations = recalculate(measurement);
        iteration++;
    }

    Calculations recalculate(double measurement) {
        if (calculations.kalmanGain() < 0.1) {
            return new Calculations(
                calculations.kalmanGain(), 
                calculations.estimate(), 
                calculations.processError()
            );
        }

        // Step 1: Calculate predicted state
        Dataset predictedState = predictState();

        throw new UnsupportedOperationException("Not implemented yet");
    }

    private Matrix stateTransition() {
        return new Matrix(new double[][] {
            { 1, timeOfOneCycle },
            { 0, 1 }
        });
    }

    /**
     * Step 1 of the Kalman Filter
     * @return
     */
    Dataset predictState() {
        Vector previousState = calculations.estimate();
        
        Vector adaptiveControl = Vector.column(new double[] {
            0.5 * Math.pow(timeOfOneCycle, 2), 
            timeOfOneCycle
        });

        Matrix controlVariable = new Matrix(2);
        
        return stateTransition()
            .times(previousState)
            .plus(
                adaptiveControl.times(controlVariable)
            );
    }

    /**
     * Equation: Observation Matrix (C) * State Matrix (X) + Measurement Input Vector (z)
     * @param Vector measurementInput 
     * @param boolean velocity
     * @return
     */
    Dataset calculateMeasurement(Vector measurementInput, boolean velocity) {
        Vector previousState = new Vector(new double[] { 20, 0 });
        if (!velocity) {
            Matrix observation = new Matrix(new double[][] {{ 1, 0}});
            return observation.times(previousState);
        } else {
            Matrix observation = new Matrix(new double[][] {{ 1, 0}, { 0, 1 }});
            return observation.times(previousState);
        }
    }

    Dataset initialProcessCovariance() {
        Matrix processCovariance = new Matrix(2, 2);

        double firstVariableProcessError = calculations.processError().get(0);
        double secondVariableProcessError = calculations.processError().get(1);

        processCovariance.set(0, 0, Math.pow(firstVariableProcessError, 2));
        processCovariance.set(0, 1, firstVariableProcessError * secondVariableProcessError);
        processCovariance.set(1, 0, firstVariableProcessError * secondVariableProcessError);
        processCovariance.set(1, 1, Math.pow(secondVariableProcessError, 2));

        return processCovariance.zeroOutMinorDiagonal();
    }

    Dataset predictProcessCovariance(Matrix noise) {
        Matrix processCovarianceMatrix = (Matrix) initialProcessCovariance();

        Matrix result = (Matrix) stateTransition().times(processCovarianceMatrix).times(stateTransition().transpose()).plus(noise);
        
        return result.zeroOutMinorDiagonal();
    }

    Dataset predictProcessCovariance() {
        Matrix processCovarianceMatrix = (Matrix) initialProcessCovariance();

        Matrix result = (Matrix) stateTransition().times(processCovarianceMatrix).times(stateTransition().transpose());

        return result.zeroOutMinorDiagonal();
    }

    Dataset calculateKalmanGain() {
        Matrix processCovarianceMatrix = (Matrix) predictProcessCovariance();
        Matrix observationMatrix = Matrix.createIdentityMatrix(2);

        Matrix numerator = (Matrix) processCovarianceMatrix.times(observationMatrix.transpose());

        Matrix denominator = (Matrix) observationMatrix.times(numerator).plus(getMeasurementCovariance());

        try {
            return numerator.divide(denominator);
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            return null;
        }
    }
}

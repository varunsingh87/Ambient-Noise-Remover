package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;

public class MultiDimensionalKalmanFilter implements KalmanFilter<Matrix> {
    private SystemCycleVector currentCycleInfo;
    private MultiDimensionalKalmanFilterEquationFactory equationFactory;
    private Matrix processNoise;

    private final static int TIME_INTERVAL = 5;

    public MultiDimensionalKalmanFilter(Vector initialEstimate, Vector initialEstUnc) {
        currentCycleInfo = new SystemCycleVector(initialEstimate, initialEstUnc);
        equationFactory = new MultiDimensionalKalmanFilterEquationFactory(TIME_INTERVAL);
        
        processNoise = Matrix.createDiagonalMatrix(initialEstimate.getRows(), 0.006);
    }

    public SystemCycleVector getCurrentCycleInfo() {
        return currentCycleInfo;
    }

    public Matrix getProcessNoise() {
        return processNoise;
    }

    public void setProcessNoise(Matrix processNoise) throws IllegalArgumentException {
        if (processNoise.isDiagonal())
            this.processNoise = processNoise;
        else
            throw new IllegalArgumentException("The process noise matrix must be a diagonal matrix");
    }

    @Override
    public Matrix calculateCurrentStateEstimate() {
        return equationFactory.useStateUpdateEquation(currentCycleInfo.getStatePrediction(), calculateKalmanGain(),
                currentCycleInfo.getStateEstimate());
    }

    @Override
    public Matrix calculateStateExtrapolation() {
        return equationFactory.useStateExtrapolationEquation(currentCycleInfo.getStatePrediction(),
                currentCycleInfo.getStateEstimate());
    }

    @Override
    public void measure(Matrix m) {
        Vector measurement = (Vector) m;
        // equationFactory
        //     .useMeasurementEquation(
        //         KalmanFilterMatrices.getObservationMatrix(
        //             currentCycleInfo.getStateEstimate().getRows(),
        //             currentCycleInfo.getStatePrediction().getColumns()
        //         ),
        //         measurement, 
        //         new Vector(
        //             new double[] { 0.0016 }
        //         )
        //     );
        currentCycleInfo.setMeasurement(measurement);
    }

    @Override
    public Matrix calculateKalmanGain() {
        try {
            return equationFactory.useKalmanGainEquation(currentCycleInfo.getStatePrediction(),
                    KalmanFilterMatrices.getObservationMatrix(currentCycleInfo.getStateEstimate().getRows(),
                            currentCycleInfo.getStatePrediction().getColumns()),
                    currentCycleInfo.getMeasurementUncertainty());
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            return new Matrix(new double[][] { { 1 } });
        }
    }

    @Override
    public Matrix calculateCurrentEstimateUncertainty() {
        // TODO: Determine estimate error
        return equationFactory.useEstimateUncertaintyEquation(new Vector(new double[] { 55, -2, 1 }));
    }

    @Override
    public Matrix calculateExtrapolatedEstimateUncertainty() {
        // TODO: Get covariance matrix
        // DONE: Get process noise matrix
        return equationFactory.useCovarianceExtrapolationEquation(
            new Matrix(new double[][] { { 7 } }),
            new Matrix(new double[][] { { 6 } })
        );
    }
}

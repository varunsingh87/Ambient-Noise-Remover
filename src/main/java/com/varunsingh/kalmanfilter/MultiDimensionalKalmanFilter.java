package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;

public class MultiDimensionalKalmanFilter implements KalmanFilter<Matrix> {
    private SystemCycleVector currentCycleInfo;
    private MultiDimensionalKalmanFilterEquationFactory equationFactory;
    private Vector processNoise;
    private Matrix processNoiseUncertainty;
    private Matrix kalmanGain;

    private final static int TIME_INTERVAL = 5;

    public MultiDimensionalKalmanFilter(Vector initialEstimate, Vector initialEstUnc) {
        currentCycleInfo = new SystemCycleVector(initialEstimate, initialEstUnc);
        equationFactory = new MultiDimensionalKalmanFilterEquationFactory(TIME_INTERVAL);
        processNoiseUncertainty = new Matrix(new double[][] {
            { 0.0005, 0.0003 },
            { 0.0003, 0.0671 }
        });
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
    }

    public SystemCycleVector getCurrentCycleInfo() {
        return currentCycleInfo;
    }

    public Vector getProcessNoise() {
        return processNoise;
    }

    public void setProcessNoise(Vector processNoise) {
        this.processNoise = processNoise;
    }

    public Matrix getProcessNoiseUncertainty() {
        return processNoiseUncertainty;
    }

    public void setProcessNoiseUncertainty(Matrix newProcessNoiseUncertainty) {
        processNoiseUncertainty = newProcessNoiseUncertainty;
    }

    public Matrix getKalmanGain() {
        return kalmanGain;
    }

    public void setKalmanGain(Matrix kalmanGain) {
        this.kalmanGain = kalmanGain;
    }

    @Override
    public Vector calculateCurrentStateEstimate() {
        return equationFactory.useStateUpdateEquation(
            currentCycleInfo.getStatePrediction(), 
            calculateKalmanGain(),
            currentCycleInfo.getStateEstimate()
        );
    }

    @Override
    public Vector calculateStateExtrapolation() {
        return equationFactory.useStateExtrapolationEquation(
            currentCycleInfo.getStatePrediction(),
            currentCycleInfo.getStateEstimate()
        );
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
        currentCycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
        currentCycleInfo.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
        currentCycleInfo.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    @Override
    public Matrix calculateKalmanGain() {
        try {
            return equationFactory.useKalmanGainEquation(
                currentCycleInfo.getStatePrediction(),
                KalmanFilterMatrices.getObservationMatrix(
                    currentCycleInfo.getStateEstimate().getRows(),
                    currentCycleInfo.getStatePrediction().getColumns()
                ),
                currentCycleInfo.getMeasurementUncertainty()
            );
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            return new Matrix(new double[][] { { 1 } });
        }
    }

    @Override
    public Matrix calculateCurrentEstimateUncertainty() {
        return equationFactory.useCovarianceUpdateEquation(
            getKalmanGain(), 
            KalmanFilterMatrices.getObservationMatrix(3, 3), 
            currentCycleInfo.getEstimateUncertaintyPrediction(), 
            currentCycleInfo.getMeasurementUncertainty()
        );
    }

    @Override
    public Matrix calculateExtrapolatedEstimateUncertainty() {
        return equationFactory.useCovarianceExtrapolationEquation(
            currentCycleInfo.getEstimateUncertainty(),
            processNoiseUncertainty
        );
    }
}

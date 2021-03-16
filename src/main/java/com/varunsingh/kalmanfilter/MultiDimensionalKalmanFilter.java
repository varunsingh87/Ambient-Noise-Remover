package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;
import com.varunsingh.linearalgebra.Vector;

public class MultiDimensionalKalmanFilter implements KalmanFilter<Matrix> {
    private SystemCycleVector currentCycleInfo;
    private MultiDimensionalKalmanFilterParameterFactory equationFactory;
    private Vector processNoise;
    private Matrix processNoiseUncertainty;
    private Matrix kalmanGain;

    private int stateVectorSize;
    private int measurementVectorSize;

    public final static int TIME_INTERVAL = 5;

    public MultiDimensionalKalmanFilter(int vec, int meas) {
        stateVectorSize = vec;
        measurementVectorSize = meas;
    }

    public void initialize(SystemCycleVector initialStateParams) {
        currentCycleInfo = initialStateParams;
        equationFactory = new MultiDimensionalKalmanFilterParameterFactory(TIME_INTERVAL);
        
        processNoiseUncertainty = generateProcessNoiseMatrix();

        currentCycleInfo.setMeasurementUncertainty(calculateMeasurementUncertainty());
        
        kalmanGain = calculateKalmanGain();
    }

    public void initialize(SystemCycleVector initialStateParams, Matrix procNoiseUnc, Matrix measUnc) {
        currentCycleInfo = initialStateParams;
        equationFactory = new MultiDimensionalKalmanFilterParameterFactory(TIME_INTERVAL);
        processNoiseUncertainty = procNoiseUnc;

        currentCycleInfo.setMeasurementUncertainty(measUnc);
        
        kalmanGain = calculateKalmanGain();
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

    public int getStateVectorSize() {
        return stateVectorSize;
    }

    public void setStateVectorSize(int stateVectorSize) {
        this.stateVectorSize = stateVectorSize;
    }

    public int getMeasurementVectorSize() {
        return measurementVectorSize;
    }

    public void setMeasurementVectorSize(int measurementVectorSize) {
        this.measurementVectorSize = measurementVectorSize;
    }

    @Override
    public Vector calculateCurrentStateEstimate() {
        return equationFactory.useStateUpdateEquation(
            currentCycleInfo.getStatePrediction(), 
            calculateKalmanGain(),
            currentCycleInfo.getMeasurement(),
            KalmanFilterMatrices.createObservationMatrix(measurementVectorSize, stateVectorSize)
        );
    }

    @Override
    public Vector calculateStateExtrapolation() {
        return equationFactory.useStateExtrapolationEquation(
            currentCycleInfo.getStatePrediction(),
            currentCycleInfo.getStateEstimate()
        );
    }

    private Matrix calculateMeasurementUncertainty() {
        return new Matrix(new double[][] {
            { 0.012, 0.605, 0.003, 0.554 },
            { 0.017, 0.324, 0.145, 0.382 },
            { 0.364, 0.879, 0.128, 0.003 }, 
            { 0.356, 0.112, 0.040, 0.103 }
        });
    }

    @Override
    public void measure(Matrix m) {
        Vector measurement = (Vector) m;

        currentCycleInfo.setMeasurement(measurement);
        kalmanGain = calculateKalmanGain();
        currentCycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
        currentCycleInfo.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
        currentCycleInfo.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    @Override
    public Matrix calculateKalmanGain() {
        try {
            return equationFactory.useKalmanGainEquation(
                currentCycleInfo.getEstimateUncertaintyPrediction(),
                KalmanFilterMatrices.createObservationMatrix(measurementVectorSize, stateVectorSize),
                currentCycleInfo.getMeasurementUncertainty()
            );
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            return new Matrix(new double[][] { { } });
        }
    }

    @Override
    public Matrix calculateCurrentEstimateUncertainty() {
        return equationFactory.useCovarianceUpdateEquation(
            getKalmanGain(), 
            KalmanFilterMatrices.createObservationMatrix(measurementVectorSize, stateVectorSize), 
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

    private Matrix generateProcessNoiseMatrix() {
        return new Matrix(new double[][] {
            { 0.005, 0.003, 0.007 },
            { 0.003, 0.067, 0.042 },
            { 0.004, 0.015, 0.1   }
        });
    }
}

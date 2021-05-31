package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;
import com.varunsingh.linearalgebra.Vector;

public class MultiDimensionalKalmanFilter implements EstimationFilter<Vector> {
    private SystemCycleVector currentCycleInfo;
    private MultiDimensionalKalmanFilterParameterFactory equationFactory;
    private CovarianceMatrixSet currentCovarianceParameters;
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
    }

    public void initialize(SystemCycleVector initialStateParams, CovarianceMatrixSet currCovParams, Matrix procNoiseUnc, Matrix measUnc) {
        currentCycleInfo = initialStateParams;
        setCurrentCovarianceParameters(currCovParams);

        equationFactory = new MultiDimensionalKalmanFilterParameterFactory(currCovParams);
        processNoiseUncertainty = procNoiseUnc;

        currentCycleInfo.setMeasurementUncertainty(measUnc);

        kalmanGain = calculateKalmanGain();
    }
    
    public CovarianceMatrixSet getCurrentCovarianceParameters() {
        return currentCovarianceParameters;
    }

    public void setCurrentCovarianceParameters(CovarianceMatrixSet currentCovarianceParameters) {
        this.currentCovarianceParameters = currentCovarianceParameters;
    }

    public boolean checkAllParametersAreInitialized() {
        return processNoiseUncertainty != null && kalmanGain != null
                && currentCycleInfo.checkAllParametersAreInitialized();
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

    private Vector calculateCurrentStateEstimate() {
        return equationFactory.useStateUpdateEquation(currentCycleInfo.getStatePrediction(), calculateKalmanGain(),
                currentCycleInfo.getMeasurement(),
                currentCovarianceParameters.getObservation());
    }

    private Vector calculateStateExtrapolation() {
        return equationFactory.useStateExtrapolationEquation(currentCycleInfo.getStatePrediction(),
                currentCycleInfo.getStateEstimate());
    }

    @Override
    public void measure(Vector measurement) {
        // if (!checkAllParametersAreInitialized()) {
        //     String errorMessage = "Not all parameters have been initialized, so measurement cannot begin";
        //     throw new NullPointerException(errorMessage);
        // }

        // currentCycleInfo.setMeasurement(measurement);
        // kalmanGain = calculateKalmanGain();
        // currentCycleInfo.setStateEstimate(calculateCurrentStateEstimate());
        // currentCycleInfo.setStatePrediction(calculateStateExtrapolation());
        // currentCycleInfo.setEstimateUncertainty(calculateCurrentEstimateUncertainty());
        // currentCycleInfo.setEstimateUncertaintyPrediction(calculateExtrapolatedEstimateUncertainty());
    }

    private Matrix calculateKalmanGain() {
        try {
            return equationFactory.useKalmanGainEquation(
                currentCycleInfo.getEstimateUncertaintyPrediction(),
                currentCovarianceParameters.getObservation(),
                currentCycleInfo.getMeasurementUncertainty()
            );
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            return new Matrix(new double[][] { {} });
        }
    }

    private Matrix calculateCurrentEstimateUncertainty() {
        return equationFactory.useCovarianceUpdateEquation(getKalmanGain(),
                currentCovarianceParameters.getObservation(),
                currentCycleInfo.getEstimateUncertaintyPrediction(), currentCycleInfo.getMeasurementUncertainty());
    }

    private Matrix calculateExtrapolatedEstimateUncertainty() {
        return equationFactory.useCovarianceExtrapolationEquation(currentCycleInfo.getEstimateUncertainty(),
                processNoiseUncertainty);
    }

    public void predict() {
        Propagation propagation = new Propagation();
        currentCycleInfo.setStatePrediction(propagation.predictNextStateVector());
        currentCycleInfo.setStatePrediction(propagation.modelNextStateVectorWithNoise());
        currentCycleInfo.setEstimateUncertaintyPrediction(propagation.modelNextEstimateUncertaintyWithNoise());
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }
}

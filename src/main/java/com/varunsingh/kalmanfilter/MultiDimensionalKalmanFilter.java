package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Vector;

public class MultiDimensionalKalmanFilter extends MeasureUpdatePredictFilter<Vector> implements KalmanFilter<Vector> {
    private final static int TIME_INTERVAL = 5;

    @Override
    public Vector calculateCurrentStateEstimate() {
        
        return null;
    }

    @Override
    public Vector calculateStateExtrapolation() {
        return null;
    }

    @Override
    public void measure(Vector measurement) {
        // TODO Auto-generated method stub

    }

    @Override
    public Vector calculateKalmanGain() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector calculateCurrentEstimateUncertainty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector calculateExtrapolatedEstimateUncertainty() {
        // TODO Auto-generated method stub
        return null;
    }

}

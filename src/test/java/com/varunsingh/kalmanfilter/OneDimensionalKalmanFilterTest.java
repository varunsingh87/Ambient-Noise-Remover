package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class OneDimensionalKalmanFilterTest {
    private OneDimensionalKalmanFilter filterWithoutProcessNoise;
    private SystemCycle sampleUncertainState;

    @Before
    public void setUp() {
        filterWithoutProcessNoise = new OneDimensionalKalmanFilter(60, 5, 15);  
        
        sampleUncertainState = new SystemCycle();
        sampleUncertainState.setStateEstimate(10);
        sampleUncertainState.setEstimateUncertainty(Math.pow(100, 2));
        sampleUncertainState.setMeasurementUncertainty(Math.pow(0.1, 2));
    }

    @Test
    public void testCalculateMeasurementUncertainty() {
        OneDimensionalKalmanFilter filter = new OneDimensionalKalmanFilter(60, 5, 15);
        OneDimensionalKalmanFilter filterWithProcessNoise = new OneDimensionalKalmanFilter(sampleUncertainState, 0.15);

        filterWithProcessNoise.measure(108.36);

        assertEquals(25, filter.getSystemState().getMeasurementUncertainty(), 0.1);
        assertEquals(0.01, filterWithProcessNoise.getSystemState().getMeasurementUncertainty(), 0.1);
    }

    @Test
    public void testCalculateCurrentState() {
        OneDimensionalKalmanFilter filterWithProcessNoise = new OneDimensionalKalmanFilter(sampleUncertainState, 0.0001);

        filterWithoutProcessNoise.measure(48.54);
        filterWithProcessNoise.measure(49.95);
        filterWithProcessNoise.measure(108.36);

        assertEquals(49.69, filterWithoutProcessNoise.getSystemState().getStateEstimate(), 0.1);
        assertEquals(79.155, filterWithProcessNoise.getSystemState().getStateEstimate(), 0.001);
    }

    @Test
    public void testCalculateStatePrediction() {
        filterWithoutProcessNoise.measure(48.54);
        assertEquals(filterWithoutProcessNoise.getSystemState().getStatePrediction(), filterWithoutProcessNoise.getSystemState().getStateEstimate(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertainty() {
        filterWithoutProcessNoise.measure(48.54);
        filterWithoutProcessNoise.measure(47.11);
        assertEquals(11.84, filterWithoutProcessNoise.getSystemState().getEstimateUncertainty(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertaintyExtrapolation_withoutProcessNoise() {
        filterWithoutProcessNoise.measure(48.54);
        filterWithoutProcessNoise.measure(47.11);
        assertEquals(filterWithoutProcessNoise.getSystemState().getEstimateUncertainty(), filterWithoutProcessNoise.getSystemState().getEstimateUncertaintyPrediction(), 0.1);
    }

    @Test
    public void testExtrapolatedEstimateUncertainty_withProcessNoise() {
        OneDimensionalKalmanFilter k2 = new OneDimensionalKalmanFilter(new SystemCycle(sampleUncertainState), 0.0001);

        k2.measure(49.95);

        double firstIterationExtrapolatedEstimateUncertainty = k2.getSystemState().getEstimateUncertaintyPrediction();
        
        k2.measure(108.36);

        double secondIterationExtrapolatedEstimateUncertainty = k2.getSystemState().getEstimateUncertaintyPrediction();

        assertEquals(0.0101, firstIterationExtrapolatedEstimateUncertainty, 0.0001);
        assertEquals(0.0051, secondIterationExtrapolatedEstimateUncertainty, 0.0001);
    }
}

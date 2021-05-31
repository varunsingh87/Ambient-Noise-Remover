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
        assertEquals(25, filter.getCycleInfo().getMeasurementUncertainty(), 0.1);
    }

    @Test
    public void testCalculateMeasurementUncertaintyWithProcessNoise() {
        OneDimensionalKalmanFilter filterWithProcessNoise = new OneDimensionalKalmanFilter(sampleUncertainState, 0.15);
        filterWithProcessNoise.measure(108.36);
        assertEquals(0.01, filterWithProcessNoise.getCycleInfo().getMeasurementUncertainty(), 0.1);
    }

    @Test
    public void testCalculateCurrentState() {
        OneDimensionalKalmanFilter filterWithProcessNoise = new OneDimensionalKalmanFilter(sampleUncertainState, 0.0001);

        filterWithoutProcessNoise.measure(48.54);
        filterWithProcessNoise.measure(49.95);
        filterWithProcessNoise.measure(108.36);

        assertEquals(49.69, filterWithoutProcessNoise.getCycleInfo().getStateEstimate(), 0.1);
        assertEquals(79.155, filterWithProcessNoise.getCycleInfo().getStateEstimate(), 0.001);
    }

    @Test
    public void testCalculateStatePrediction() {
        filterWithoutProcessNoise.measure(48.54);
        assertEquals(filterWithoutProcessNoise.getCycleInfo().getStatePrediction(), filterWithoutProcessNoise.getCycleInfo().getStateEstimate(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertainty() {
        filterWithoutProcessNoise.measure(48.54);
        filterWithoutProcessNoise.measure(47.11);
        assertEquals(11.84, filterWithoutProcessNoise.getCycleInfo().getEstimateUncertainty(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertaintyExtrapolation_withoutProcessNoise() {
        filterWithoutProcessNoise.measure(48.54);
        filterWithoutProcessNoise.measure(47.11);
        assertEquals(filterWithoutProcessNoise.getCycleInfo().getEstimateUncertainty(), filterWithoutProcessNoise.getCycleInfo().getEstimateUncertaintyPrediction(), 0.1);
    }

    @Test
    public void testExtrapolatedEstimateUncertainty_withProcessNoise() {
        OneDimensionalKalmanFilter k2 = new OneDimensionalKalmanFilter(new SystemCycle(sampleUncertainState), 0.0001);

        k2.measure(49.95);

        double firstIterationExtrapolatedEstimateUncertainty = k2.getCycleInfo().getEstimateUncertaintyPrediction();
        
        k2.measure(108.36);

        double secondIterationExtrapolatedEstimateUncertainty = k2.getCycleInfo().getEstimateUncertaintyPrediction();

        assertEquals(0.0101, firstIterationExtrapolatedEstimateUncertainty, 0.0001);
        assertEquals(0.0051, secondIterationExtrapolatedEstimateUncertainty, 0.0001);
    }
}

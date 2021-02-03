package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PositionalKalmanFilterTest {
    private PositionalKalmanFilter filter;

    @Before
    public void setUp() {
        filter = new PositionalKalmanFilter(60, 5, 15);    
    }

    @Test
    public void testCalculateMeasurementUncertainty() {
        assertEquals(filter.getSystemState().getMeasurementUncertainty(), 25, 0.1);
    }

    @Test
    public void testCalculateInitialEstimateUncertainty() {
        assertEquals(filter.getSystemState().getEstimateUncertainty(), 225, 0.1);
    }

    @Test
    public void testCalculateCurrentState() {
        filter.measure(48.54);

        assertEquals(filter.getSystemState().getStateEstimate(), 49.69, 0.1);
    }

    @Test
    public void testCalculateStatePrediction() {
        filter.measure(48.54);
        assertEquals(filter.getSystemState().getStatePrediction(), filter.getSystemState().getStateEstimate(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertainty() {
        filter.measure(48.54);
        filter.measure(47.11);
        assertEquals(11.84, filter.getSystemState().getEstimateUncertainty(), 0.1);
    }

    @Test
    public void testCalculateEstimateUncertaintyExtrapolation() {
        filter.measure(48.54);
        filter.measure(47.11);
        assertEquals(filter.getSystemState().getEstimateUncertainty(), filter.getSystemState().getEstimateUncertaintyPrediction(), 0.1);
    }
}

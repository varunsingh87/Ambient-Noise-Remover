package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AlphaBetaGammaFilterTest {
    private AlphaBetaGammaFilter filter;
    private SystemCycle[] systemData = { 
        new SystemCycle(30_160, 30_205, 42.8, -0.7, 30_410, 39.2),
        new SystemCycle(30_365, 30_387.5, 35.6, -1.1, 30_552, 30.2),
        new SystemCycle(30_890, 30_721, 57.2, 1.6, 31_027.5, 65.4) 
    };

    @Before
    public void setUp() {
        filter = new AlphaBetaGammaFilter(new SystemCycle(30_000, 50, 0), 0.5, 0.4, 0.1);
    }

    @Test
    public void testCalculateCurrentState() {
        for (SystemCycle state : systemData) {
            filter.measure(state.getMeasurement());
            assertEquals(state.getStateEstimate(), filter.getSystemState().getStateEstimate(), 0.1);
        }
    }

    @Test
    public void testCalculateCurrentVelocity() {
        for (SystemCycle state : systemData) {
            filter.measure(state.getMeasurement());
            assertEquals(state.getStateVelocity(), filter.getSystemState().getStateVelocity(), 0.1);
        }
    }

    @Test
    public void testCalculateCurrentAcceleration() {
        for (SystemCycle state : systemData) {
            filter.measure(state.getMeasurement());
            assertEquals(state.getStateAcceleration(), filter.getSystemState().getStateAcceleration(), 0.1);
        }
    }

    @Test
    public void testCalculateStatePrediction() {
        for (SystemCycle state : systemData) {
            filter.measure(state.getMeasurement());
            assertEquals(state.getStatePrediction(), filter.getSystemState().getStatePrediction(), 0.1);
        }
    }

    @Test
    public void testCalculateVelocityPrediction() {
        for (SystemCycle state : systemData) {
            filter.measure(state.getMeasurement());
            assertEquals(state.getVelocityPrediction(), filter.getSystemState().getVelocityPrediction(), 0.1);
        }
    }
}

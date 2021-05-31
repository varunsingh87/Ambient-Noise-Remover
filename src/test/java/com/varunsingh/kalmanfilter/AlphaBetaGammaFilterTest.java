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

    /**
     * Unit test for Alpha-Beta-Gamma Filter algorithm
     * Tests that the state values are correct
     */
    @Test
    public void testAlphaBetaGammaFilterTest() {
        for (SystemCycle state : systemData) {
            filter.runAlgorithm(state.getMeasurement());
            assertEquals(state.getStateEstimate(), filter.getCycleInfo().getStateEstimate(), 0.1);
            assertEquals(state.getStateVelocity(), filter.getCycleInfo().getStateVelocity(), 0.1);
            assertEquals(state.getStateAcceleration(), filter.getCycleInfo().getStateAcceleration(), 0.1);
            assertEquals(state.getStatePrediction(), filter.getCycleInfo().getStatePrediction(), 0.1);
            assertEquals(state.getVelocityPrediction(), filter.getCycleInfo().getVelocityPrediction(), 0.1);
        }
    }
    
}

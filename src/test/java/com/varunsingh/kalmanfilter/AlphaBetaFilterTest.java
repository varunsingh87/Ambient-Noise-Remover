package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AlphaBetaFilterTest {
    AlphaBetaFilter filter;

    double[][] systemData = new double[][] { 
        { 30110, 30182, 38.2, 30373 }, 
        { 30265, 30351.4, 36, 30531.6 },
        { 30740, 30573.3, 40.2, 30774.3 }, 
        { 30750, 30769.5, 39.7, 30968.1 }, 
        { 31135, 31001.5, 43.1, 31216.8 },
        { 31015, 31176.4, 39, 31371.5 }, 
        { 31180, 31333.2, 35.2, 31509.2 }, 
        { 31610, 31529.4, 37.2, 31715.4 },
        { 31960, 31764.3, 42.1, 31974.8 }, 
        { 31865, 31952.9, 39.9, 32152.4 } 
    };

    @Before
    public void setUp() {
        filter = new AlphaBetaFilter(30000, 40, 0.2, 0.1);
    }

    /**
     * Unit test for alpha beta filter class
     */
    @Test
    public void testAlphaBetaFilter() {
        for (int i = 0; i < systemData.length; i++) {
            // Arrange
            double[] currentIteration = systemData[i];
            
            // Act
            filter.runAlgorithm((double) currentIteration[0]);

            // Assert
            assertEquals((double) currentIteration[1], filter.getCycleInfo().getStateEstimate(), 0.1);
            assertEquals((double) currentIteration[2], filter.getCycleInfo().getStateVelocity(), 0.1);
            assertEquals((double) currentIteration[3], filter.getCycleInfo().getStatePrediction(), 0.1);
        }
    }
}

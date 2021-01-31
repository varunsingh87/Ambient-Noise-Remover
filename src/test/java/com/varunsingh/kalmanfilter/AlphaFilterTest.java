package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AlphaFilterTest {
    AlphaFilter alphaFilter;

    @Before
    public void setUp() {
        alphaFilter = new AlphaFilter(1000);
    }

    @Test
    public void testIterationIncrement() {
        // Act
        alphaFilter.measure(60);
        int iterationAfter1Measurement = alphaFilter.getIteration();

        alphaFilter.measure(67);
        int iterationAfter2Measurements = alphaFilter.getIteration();

        // Assert
        assertEquals(1, iterationAfter1Measurement);
        assertEquals(2, iterationAfter2Measurements);
    }

    @Test
    public void testKalmanGainEquation() {
        alphaFilter.measure(45.5);
        double kalmanGainAfter1Iteration = alphaFilter.calculateAlphaFilter();
        
        alphaFilter.measure(52.3);
        double kalmanGainAfter2Iterations = alphaFilter.calculateAlphaFilter();

        alphaFilter.measure(56.0);
        double kalmanGainAfter3Iterations = alphaFilter.calculateAlphaFilter();

        assertEquals(1.0, kalmanGainAfter1Iteration, 0.001);
        assertEquals(0.5, kalmanGainAfter2Iterations, 0.001);
        assertEquals(1.0 / 3.0, kalmanGainAfter3Iterations, 0.001);
    }

    @Test
    public void testStateUpdateEquation() {
        // Arrange
        int firstMeasurement = 1030;
        int secondMeasurement = 989;
        int thirdMeasurement = 1017;

        // Act
        alphaFilter.measure(firstMeasurement);
        double stateAfterFirstIteration = alphaFilter.getCurrentState();

        alphaFilter.measure(secondMeasurement);
        double stateAfterSecondIteration = alphaFilter.getCurrentState();

        alphaFilter.measure(thirdMeasurement);
        double stateAfterThirdIteration = alphaFilter.getCurrentState();

        // Assert
        assertEquals(firstMeasurement, stateAfterFirstIteration, 0.01);
        assertEquals(1009.5, stateAfterSecondIteration, 0.01);
        assertEquals(1012, stateAfterThirdIteration, 0.01);
    }
    
    @Test
    public void testStateExtrapolationEquation() {
        assertEquals(1000, alphaFilter.calculatePrediction(), 0.1);
    }
}

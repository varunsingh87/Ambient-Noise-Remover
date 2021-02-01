package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MeasureUpdatePredictFilterTest {
	MeasureUpdatePredictFilter measureUpdatePredictFilter;

    @Before
    public void setUp() {
        measureUpdatePredictFilter = new MeasureUpdatePredictFilter(200) {
            @Override
            public double calculateCurrentStateEstimate() { return 0; }

            @Override
            public double calculatePrediction() { return 0; }
        };
    }

    @Test
    public void testIterationIncrement() {
        // Act
        measureUpdatePredictFilter.measure(60);
        int iterationAfter1Measurement = measureUpdatePredictFilter.getIteration();

        measureUpdatePredictFilter.measure(67);
        int iterationAfter2Measurements = measureUpdatePredictFilter.getIteration();

        // Assert
        assertEquals(1, iterationAfter1Measurement);
        assertEquals(2, iterationAfter2Measurements);
    }
}

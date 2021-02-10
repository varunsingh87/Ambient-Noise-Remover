package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MeasureUpdatePredictFilterTest {
	MeasureUpdatePredictFilter<Double> measureUpdatePredictFilter;

    @Before
    public void setUp() {
        measureUpdatePredictFilter = new MeasureUpdatePredictFilter<Double>(200.0) {
            @Override
            public Double calculateCurrentStateEstimate() { return 0.0; }

            @Override
            public Double calculateStateExtrapolation() { return 0.0; }
        };
    }

    @Test
    public void testIterationIncrement() {
        // Act
        measureUpdatePredictFilter.measure(60.0);
        int iterationAfter1Measurement = measureUpdatePredictFilter.getIteration();

        measureUpdatePredictFilter.measure(67.0);
        int iterationAfter2Measurements = measureUpdatePredictFilter.getIteration();

        // Assert
        assertEquals(1, iterationAfter1Measurement);
        assertEquals(2, iterationAfter2Measurements);
    }
}

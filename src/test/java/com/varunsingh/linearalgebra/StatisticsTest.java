package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StatisticsTest {
    private Vector dataset;

    public StatisticsTest() {
        double[] data = new double[] { 2, 4, 5, 7, 7 };
        dataset = new Vector(data);
    }

    @Test
    public void testVarianceCalculation() {
        double expectedVariance = 3.6;
        double actualVariance = dataset.calcVariance();
        
        assertEquals(expectedVariance, actualVariance, 0.1);
    }
    
    @Test
    public void testStandardDeviationCalculation() {
        double expectedStdDev = 1.9;
        double actualStdDev = dataset.calcStandardDeviation();
        
        assertEquals(expectedStdDev, actualStdDev, 0.1);
    }
}

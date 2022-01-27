package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

import org.junit.Before;
import org.junit.Test;

public class KalmanFilterTest {
    private KalmanFilter algorithm;
    
    @Before
    public void initialize() {
        algorithm = new KalmanFilter(Vector.column(4000, 280), Vector.column(25, 6), Vector.column(20, 5));
    }

    @Test
    public void testPredictState() {
        Dataset predictedState = algorithm.predictState();
        assertEquals(Vector.column(4281, 282), predictedState);
    }

    @Test
    public void testInitialProcessCovariance() {
        Dataset initialProcessCovariance = algorithm.initialProcessCovariance();
        Matrix expected = new Matrix(new double[][] {
            { 400, 0 },
            { 0, 25 }
        });
        assertEquals(expected, initialProcessCovariance);
    }

    @Test
    public void testPredictedProcessCovariance() {
        Dataset predictedProcessCovariance = algorithm.predictProcessCovariance();
        Matrix expected = new Matrix(new double[][] {
            { 425, 0 },
            { 0, 25 }
        });

        assertEquals(expected, predictedProcessCovariance);
    }

}

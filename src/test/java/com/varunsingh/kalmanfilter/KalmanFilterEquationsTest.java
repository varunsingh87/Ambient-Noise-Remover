package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class KalmanFilterEquationsTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 30200,   40,   30110, 30182,   38.2, 30373 },
            { 30373,   38.2, 30265, 30351.4, 36,   30531.6 },
            { 30531.6, 36,   30740, 30573.3, 40.2, 30774.3 },
            { 30774.3, 40.2, 30750, 30769.5, 39.7, 30968.1 }, 
            { 31715.4, 37.2, 31960, 31764.3, 42.1, 31974.8 },
            { 31974.8, 42.1, 31865, 31952.9, 39.9, 32152.4 }
        });
    }

    private double previousStateEstimate;
    private double previousVelocityEstimate;
    private double measurement;
    private double expectedStateEstimate;
    private double expectedVelocityEstimate;
    private double expectedStatePrediction;

    private final double ALPHA_FILTER = 0.2;
    private final double BETA_FILTER = 0.1;

    public KalmanFilterEquationsTest(double prevStateEst, double prevVelEst, double m, double expStateEst, double expVelEst, double expStatePred) {
        previousStateEstimate = prevStateEst;
        previousVelocityEstimate = prevVelEst;
        measurement = m;
        expectedStateEstimate = expStateEst;
        expectedVelocityEstimate = expVelEst;
        expectedStatePrediction = expStatePred;
    }
    
    @Test
    public void testPositionalStateUpdateEquation() {
        double result = KalmanFilterEquations.usePositionalStateUpdateEquation(
            previousStateEstimate, 
            ALPHA_FILTER, 
            measurement
        );
        
        assertEquals(expectedStateEstimate, result, 0.1);
    }

    @Test
    public void testVelocityStateUpdateEquation() {
        double result = KalmanFilterEquations.useVelocityStateUpdateEquation(
            previousStateEstimate,
            previousVelocityEstimate, 
            BETA_FILTER, 
            measurement, 
            5
        );
        assertEquals(expectedVelocityEstimate, result, 0.1);
    }

    @Test
    public void testPositionalStateExtrapolationEquation() {
        double result = KalmanFilterEquations.usePositionalStateExtrapolationEquation(expectedStateEstimate, 5, expectedVelocityEstimate);
        assertEquals(expectedStatePrediction, result, 0.9);
    }

    @Test
    public void test_PositionalStateExtrapolationEquation_With_Velocity_And_Acceleration() {
        double result = KalmanFilterEquations.usePositionalStateExtrapolationEquation(31038.8, 5, 67.2, 1.8);
        assertEquals(31_397.1, result, 0.9);
    }
}

package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertTrue;

import com.varunsingh.linearalgebra.Matrix;

import org.junit.Test;

public class KalmanFilterMatricesTest {
    @Test
    public void testGetIdentityMatrix() {
        Matrix threeDimenIdentity = KalmanFilterMatrices.getIdentityMatrix(3);
        
        assertTrue(threeDimenIdentity.isIdentityMatrix());
    }
}

package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.varunsingh.linearalgebra.Vector.VectorType;

import org.junit.Test;

public class VectorTest {
    @Test
    public void testTranspose() {
        assertEquals(new Vector(new double[] { 5, 7, 6 }), new Vector(new double[] { 5, 7, 6 }, VectorType.COLUMN));
    }

    @Test
    public void testCalcLength() {
        Vector sampleVector = new Vector(new double[] { 2, 5 });

        double calculatedLength = sampleVector.calcLength();

        assertEquals(Math.sqrt(29), calculatedLength, 0.0);
    }

    @Test
    public void testLengthSatisfiesTriangleInequality() {
        Vector x = new Vector(new double[] { 4, 5, 6 });
        Vector y = new Vector(new double[] { 6, -2, -3 });

        double lengthOfSum = x.plus(y).asRowVector().calcLength();
        double sumOfLengths = x.calcLength() + y.calcLength();

        assertTrue(lengthOfSum <= sumOfLengths);
    }

    /**
     * Tests that the length of the sum of two vectors is equal to the sum of the
     * lengths of the vectors if one vector is a scaled up version of the other two
     * vectors (x = cy) nicknamed Triangle Equality here
     */
    @Test
    public void testLengthSatisfiesTriangleEquality() {
        Vector x = new Vector(new double[] { 4, 5, 6 });
        Vector y = x.scale(6);

        double lengthOfSum = x.plus(y).asColumnVector().calcLength();
        double sumOfLengths = x.calcLength() + y.calcLength();

        assertEquals(lengthOfSum, sumOfLengths, 0.0);
    }

    
}

package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

        double lengthOfSum = x.plus(y).calcLength();
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

        double lengthOfSum = x.plus(y).calcLength();
        double sumOfLengths = x.calcLength() + y.calcLength();

        assertEquals(lengthOfSum, sumOfLengths, 0.0);
    }

    @Test
    public void testCrossProductWithValidFactors() {
        Vector x = new Vector(new double[] { 1, -7, 1 });
        Vector y = new Vector(new double[] { 5, 2, 4 });

        Vector crossProduct = x.cross(y);
        Vector expectedResult = new Vector(new double[] { -30, 1, 37 });

        assertEquals(expectedResult, crossProduct);
        assertEquals(0, crossProduct.dot(x), 0.0);
        assertEquals(0, crossProduct.dot(y), 0.0);
    }

    @Test
    public void testCrossProductWithInvalidFactors() {
        Vector x = new Vector(new double[] { 11, 20 });
        Vector y = new Vector(new double[] { 52, 60, 30 });

        try {
            x.cross(y);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testDotProductEqualsProductOfMagnitudeTimesCosine() {
        Vector a = new Vector(new double[] { 1, 2, 3 });
        Vector b = new Vector(new double[] { -8, -9, -10 });

        double dotProduct = a.dot(b);
        double productOfMagnitudesAndCosine = a.calcMagnitude() * b.calcMagnitude() * Math.cos(2.8444);
        
        assertEquals(dotProduct, productOfMagnitudesAndCosine, 0.1);
    }

    @Test
    public void testMagnitudeOfCrossProductEqualsProductOfMagnitudeTimesSine() {
        Vector a = new Vector(new double[] { 5, -9, 2 });
        Vector b = new Vector(new double[] { -2, 10, 8 });

        double crossProductMagnitude = a.cross(b).calcMagnitude();

        assertEquals(106.883, crossProductMagnitude, 0.001);
        assertEquals(crossProductMagnitude, a.calcMagnitude() * b.calcMagnitude() * Math.sin(0.904709029), 0.001);
    }

    /**
     * @see https://en.wikipedia.org/wiki/Triple_product#Vector_triple_product
     */
    @Test
    public void testTripleProductExpansion() {
        Vector a = new Vector(new double[] { 5, 6, 7 });
        Vector b = new Vector(new double[] { 1, 2, 3 });
        Vector c = new Vector(new double[] { -1, -9, 8 });

        Vector tripleCrossProduct = a.cross(b.cross(c));
        Vector lagrangeFormulaRightHandSide = b
            .scale(
                a.dot(c)
            )
            .minus(
                c.scale(
                    a.dot(b)
                )
            ); 

        assertEquals(lagrangeFormulaRightHandSide, tripleCrossProduct);
    }
}

package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the dot product properties of two or more vectors
 * @see Vector#dot(Vector)
 */
public class VectorDotProductTest {
    @Test
    public void testDotProduct() {
        Vector w = new Vector(new double[] { 5, 6, 7, 8, -2 });
        Vector v = new Vector(new double[] { -2, -1, 0, 3, -1 });

        assertEquals(10.0, w.dot(v), 0.0);
    }

    @Test
    public void testDotProductIsCommutative() {
        Vector w = new Vector(new double[] { 3, 4, 7, 8, -3 });
        Vector v = new Vector(new double[] { -2, -2, 0, 3, -1 });

        double wDotV = w.dot(v);
        double vDotW = v.dot(w);

        assertEquals(wDotV, vDotW, 0.0);
    }

    @Test
    public void testDotProductIsDistributive() {
        Vector factor = new Vector(new double[] { 1, 3, -6 });
        Vector x = new Vector(new double[] { 2, 3, 4 });
        Vector y = new Vector(new double[] { -8, -9, -10 });

        double productOfFactorAndQuantity = factor.dot(x.plus(y));
        double sumOfDistributions = factor.dot(x) + factor.dot(y);
        
        assertEquals(productOfFactorAndQuantity, sumOfDistributions, 0.0);
    }

    @Test
    public void testDotProductIsAssociative() {
        double scalar = 10;
        Vector v = new Vector(new double[] { 5, -13 });
        Vector w = new Vector(new double[] { 16, 17 });

        double vScaledDotW = v.scale(scalar).dot(w);
        double vDotWProductScaled = v.dot(w) * scalar;

        assertEquals(vScaledDotW, vDotWProductScaled, 0.0);
    }

    @Test
    public void testLengthSquared_IsDotProductOfItself() {
        Vector sampleVector = new Vector(new double[] { 1, -7 });

        double dotProduct = sampleVector.dot();
        double lengthSquared = Math.pow(sampleVector.calcLength(), 2);
        
        assertEquals(lengthSquared, dotProduct, 0.0001);
    }

    @Test
    public void testCauchySchwarzInequality() {
        Vector v = new Vector(new double[] { -10, 23, 42, 22 });
        Vector w = new Vector(new double[] { 2, -10, 8, 9 });

        double absoluteValueOfDotProduct = Math.abs(v.dot(w));
        double productOfLengths = v.calcLength() * w.calcLength();

        assertTrue(productOfLengths >= absoluteValueOfDotProduct);
    }

    /**
     * Tests that the products of the lengths of two vectors 
     * is equal to the absolute value of the two vectors
     * if one vector is a scaled up version of the other vector,
     * nicknamed the Cauchy-Schwarz Equality here
     */
    @Test
    public void testCauchySchwarzEquality() {
        Vector v = new Vector(new double[] { -5, -10, 23, 46, 32, 42, 21, 22, 2, -10, 9, 8, 4, 18 });
        Vector w = v.scale(3);

        double absoluteValueOfDotProduct = Math.abs(v.dot(w));
        double productOfLengths = v.calcLength() * w.calcLength();

        assertEquals(productOfLengths, absoluteValueOfDotProduct, 0.0);
    }
}

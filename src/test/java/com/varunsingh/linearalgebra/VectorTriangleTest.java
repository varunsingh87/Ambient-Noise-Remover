package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTriangleTest {
    @Test
    public void test_givenAIsScalarMultipleOfB_andScalarIsGreaterThanZero_whenTriangulated_thenAngleBetweenIsZero() {
        Vector a = new Vector(new double[] { 5, 8, 9 });
        Vector b = a.scale(8);

        VectorTriangle triangle = new VectorTriangle(a, b);
        double angleBetweenA_andB = triangle.getAngle((short) 1);

        assertEquals(0, angleBetweenA_andB, 0.0);
    }

    @Test
    public void test_givenAIsScalarMultipleOfB_andScalarIsLessThanZero_whenTriangulated_thenAngleBetweenIs180() {
        Vector a = new Vector(new double[] { -11, -2, -4 });
        Vector b = a.scale(-2);

        VectorTriangle triangle = new VectorTriangle(a, b);
        double angleBetweenA_andB = triangle.getAngle((short) 1);

        assertEquals(180, angleBetweenA_andB, 0.0);
    }

    @Test
    public void test_givenNonZeroA_dotNonZeroB_isZero_whenTriangulated_thenAngleBetweenIsPerpendicular() {
        Vector a = new Vector(new double[] { -1, 4, 30 });
        Vector b = new Vector(new double[] { 50, -25, 5 });

        VectorTriangle triangle = new VectorTriangle(a, b);
        double angleBetweenA_andB = triangle.getAngle((short) 1);

        assertEquals(90, angleBetweenA_andB, 0.0);
    }
}

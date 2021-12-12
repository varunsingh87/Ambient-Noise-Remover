package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class VectorMatrixMultiplication {
    @Test
    public void testMatrixVectorMultiplication() {
        // 3x3 matrix
        double[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[] vector = {1, 2, 3}; // 3x1 column vector

        Dataset result = new Matrix(matrix).times(new Vector(vector));
        double[] expected = {14, 32, 50};
        
        assertEquals(new Vector(expected), result);
    }

    @Test
    public void testVectorMatrixMultiplication() {
        Vector row = Vector.row(13, 7, -2, 4, 2);
        Matrix mat = new Matrix(new double[][] {
            { 28, 18,  9 },
            {  3,  7,  6 },
            {  5,  5,  0 },
            { -5,  2,  2 },
            {  3,  4,  2 }
        });

        Dataset actualProduct = row.times(mat);
        Vector expectedProduct = Vector.row(361, 289, 171);

        assertEquals(expectedProduct, actualProduct);
    }
}

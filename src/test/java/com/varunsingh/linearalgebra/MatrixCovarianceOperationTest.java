package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MatrixCovarianceOperationTest {
    @Test
    public void testCompute() {
        Vector apples = new Vector(new double[] { 1, 3, -1 });
        Vector bananas = new Vector(new double[] { 1, 0, -1 });
        MatrixCovarianceOperation operation = new MatrixCovarianceOperation(apples, bananas);

        double[][] resultingCovarianceMatrixElements = operation.compute().getMatrixElements();
        double[][] expectedCovarianceMatrixElements = new double[][] { { 8.0 / 3.0, 2.0 / 3.0 },
                { 2.0 / 3.0, 2.0 / 3.0 } };

        assertArrayEquals(expectedCovarianceMatrixElements, resultingCovarianceMatrixElements);
    }
}

package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixCovarianceOperationTest {
    @Test
    public void testCompute2D() {
        Vector apples = new Vector(new double[] { 1, 3, -1 });
        Vector bananas = new Vector(new double[] { 1, 0, -1 });
        MatrixCovarianceOperation operation = new MatrixCovarianceOperation(apples, bananas);

        Matrix resultingCovarianceMatrixElements = operation.compute();
        Matrix expectedCovarianceMatrixElements = MatrixRound.roundMatrix(new Matrix(new double[][] { { 8.0 / 3.0, 2.0 / 3.0 },
                { 2.0 / 3.0, 2.0 / 3.0 } }), 5);

        assertEquals(expectedCovarianceMatrixElements, resultingCovarianceMatrixElements);
    }

    @Test
    public void testCompute3D() {
        Vector lengths = new Vector(new double[] { 4.0, 4.2, 3.9, 4.3, 4.1 });
        Vector widths = new Vector(new double[] { 2.0, 2.1, 2.0, 2.1, 2.3 });
        Vector heights = new Vector(new double[] { 0.60, 0.59, 0.56, 0.62, 0.63 });

        Matrix generatedCovarianceMatrix = new MatrixCovarianceOperation(lengths, widths, heights).compute();
        
        assertEquals(new Matrix(new double[][] { { 0.02, 0.006, 0.0026 }, { 0.006, 0.012, 0.002 }, { 0.0026, 0.002, 0.0006  } }), generatedCovarianceMatrix);
    }
}

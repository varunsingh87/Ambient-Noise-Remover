package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixCovarianceOperationTest {
    @Test
    public void testCompute2D() {
        Vector trip1 = new Vector(new double[] { 1, 1 });
        Vector trip2 = new Vector(new double[] { 3, 0 });
        Vector trip3 = new Vector(new double[] { -1, -1 });

        MatrixCovarianceOperation operation = new MatrixCovarianceOperation(trip1, trip2, trip3);

        Matrix resultingCovarianceMatrixElements = operation.compute();
        Matrix expectedCovarianceMatrixElements = MatrixRound.roundMatrix(new Matrix(new double[][] { 
            { 8.0 / 3.0, 2.0 / 3.0 },
            { 2.0 / 3.0, 2.0 / 3.0 } 
        }), 5);

        assertEquals(expectedCovarianceMatrixElements, resultingCovarianceMatrixElements);
    }

    @Test
    public void testCompute3D() {
        Vector set1 = new Vector(new double[] { 4.0, 2.00, 0.60 });
        Vector set2 = new Vector(new double[] { 4.2, 2.10, 0.59 });
        Vector set3 = new Vector(new double[] { 3.9, 2.00, 0.56 });
        Vector set4 = new Vector(new double[] { 4.3, 2.10, 0.62 });
        Vector set5 = new Vector(new double[] { 4.1, 2.30, 0.63 });

        Matrix generatedCovarianceMatrix = new MatrixCovarianceOperation(set1, set2, set3, set4, set5).compute();
        
        assertEquals(new Matrix(new double[][] { { 0.02, 0.006, 0.0022 }, { 0.006, 0.012, 0.002 }, { 0.0022, 0.002, 0.0006  } }), generatedCovarianceMatrix);
    }

    // Merge together using multiple datasets feature
    @Test
    public void testCompute3DCaseTwo() {
        Vector set1 = new Vector(new double[] { 90, 80, 40 });
        Vector set2 = new Vector(new double[] { 90, 60, 80 });
        Vector set3 = new Vector(new double[] { 60, 50, 70 });
        Vector set4 = new Vector(new double[] { 30, 40, 70 });
        Vector set5 = new Vector(new double[] { 30, 20, 90 });

        Matrix generatedCovarianceMatrix = new MatrixCovarianceOperation(set1, set2, set3, set4, set5).compute();
        
        assertEquals(new Matrix(new double[][] { { 720, 480.0, -240.0 }, { 480.0, 400.0, -280.0 }, { -240.0, -280.0, 280.0  } }), generatedCovarianceMatrix);
    }
}

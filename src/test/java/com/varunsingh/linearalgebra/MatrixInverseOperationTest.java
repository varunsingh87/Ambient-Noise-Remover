package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;

import org.junit.Test;

public class MatrixInverseOperationTest {
    @Test
    public void testIsRunnable() {
        try {
            new MatrixInverseOperation(new double[][] { { 3.0, 1.0 }, { 2.0, 2.0 } }).compute();
        } catch (MatrixNotInvertibleException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInverseWith2x2() {
        Matrix expectedInverse = new Matrix(new double[][] { { 1.0 / 1050.0, 0 }, { 0, 1.0 / 61.0 }});
        Matrix m = new Matrix(new double[][] { { 1050, 0 }, { 0, 61 }});
        try {
            Matrix actualInverse = new MatrixInverseOperation(m).compute(3);
            assertEquals(MatrixRound.roundMatrix(expectedInverse, 3), actualInverse);
        } catch (MatrixNotInvertibleException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testInverseWith5x5() {
        try {
            MatrixInverseOperation origMatrix = new MatrixInverseOperation(new double[][] {
                { 9, 6, 13, 7, 3  },
                { 14, 2, 8, 4, 10  },
                { 5, 11, 12, 15, 16 },
                { 17, 18, 19, 20, 21 },
                { 22, 23, 24, 25, 26 }
            });
            assertArrayEquals(new double[][] {
                { 0, -0.143, -0.714, 5.6, -4.029 },
                { -0.091, 0.221, 1.013, -10.418, 7.717 },
                { 0.091, 0.279, 0.987, -8.182, 5.883 },
                { 0.091, -0.649, -2.156, 20.018, -14.603 },
                { -0.091, 0.292, 0.870, -7.218, 5.231 }
            }, origMatrix.compute().getMatrixElements());
        } catch (Matrix.MatrixNotInvertibleException e) {
            e.printStackTrace();
            fail("Incorrectly threw a MatrixNotInvertibleException");
        }
    }

}

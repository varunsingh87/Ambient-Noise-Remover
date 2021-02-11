package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.varunsingh.kalmanfilter.KalmanFilterMatrices;
import com.varunsingh.linearalgebra.Matrix.MatrixNotInvertibleException;

import org.junit.Test;

public class MatrixTest {
    @Test
    public void testMatrixCanBeInstantiated() {
        new Matrix(new double[][] { { 10, 5, 3 }, { 3, 2, 2 } });
        new Matrix();
    }

    @Test
    public void testMatrixCanBeCompared() {
        Matrix comparerMatrix = new Matrix(new double[][] { { 10, 5, 10 }, { 20, 5, 20 } });

        Matrix compareeMatrix = new Matrix(new double[][] { { 10, 5, 10 }, { 20, 5, 20 } });

        Matrix unequalMatrix = new Matrix(new double[][] { { 1, 2, 3 }, { 3, 1, 2 }, { 2, 3, 1 } });

        assertEquals(comparerMatrix, compareeMatrix);
        assertNotEquals(comparerMatrix, unequalMatrix);
    }

    @Test
    public void testMatrixByVectorMultiplication() {
        Matrix m = new Matrix(new double[][] { { 5.0, 5.0, 5.0 } });
        Vector v = new Vector(5, 5, 5);

        Matrix result = v.times(m);

        assertEquals(3, result.getRows());
        assertEquals(3, result.getColumns());
        assertEquals(new Matrix(new double[][] { { 25.0, 25.0, 25.0 }, { 25.0, 25.0, 25.0 }, { 25.0, 25.0, 25.0 } }),
                result);
    }

    @Test
    public void testImpossibleMatrixMultiplication() {
        try {
            Matrix m = new Matrix(new double[][] { { 5, 10 }, { 10, 40 } });
            Vector m2 = new Vector(10, 5, 7, 6, -8, 0);

            m.times(m2);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testMatrixAddition() {
        Matrix addend = new Matrix(new double[][] { { 1, 2, 3 }, { 4, 5, 6 } });
        Matrix augend = new Matrix(new double[][] { { 10, 14, -8 }, { 3, 1, 5 } });

        Matrix sum = addend.plus(augend);

        assertArrayEquals(new double[][] { { 11, 16, -5 }, { 7, 6, 11 } }, sum.getMatrixElements());
    }

    @Test
    public void testImpossibleMatrixAddition() {
        Matrix addend = new Matrix(new double[][] { { 1, 6 } });
        Matrix invalidAugend = new Matrix(new double[][] { { 3, 3, 3 } });

        try {
            addend.plus(invalidAugend);
            fail("Incorrectly attempted to add incompatible matrices");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testIsSquareMatrix() {
        Matrix nonSquare = new Matrix(new double[][] { { 10, 20 }, { 20, 21 }, { 7, 6 } });
        Matrix square = new Matrix(new double[][] { { 10, 20 }, { 30, 40 } });
        assertFalse(nonSquare.isSquare());
        assertTrue(square.isSquare());
    }

    @Test
    public void testIsIdentityMatrix() {
        Matrix nonIdentity = new Matrix(new double[][] { { 6 }, { 7 }, { 8 } });
        Matrix identityMatrix = new Matrix(new double[][] { { 1, 0 }, { 0, 1 } });

        assertFalse(nonIdentity.isIdentityMatrix());
        assertTrue(identityMatrix.isIdentityMatrix());
    }

    @Test
    public void testTranspose() {
        Matrix origMatrix = new Matrix(new double[][] { { 1, 2 }, { 3, 4 }, { 1, 2 } });

        Matrix transposedMatrix = origMatrix.transpose();
        double[][] expectedElements = new double[][] { { 1, 3, 1 }, { 2, 4, 2 } };
        assertArrayEquals(expectedElements, transposedMatrix.getMatrixElements());
    }

    @Test
    public void testInverseWith3x3() {
        Matrix origMatrix = new Matrix(new double[][] { 
            { 1, -1, 3 },
            { 2, 1,  2 },
            { -2, -2, 1 }
        });

        try {
            Matrix invertedMatrix = origMatrix.invert();
            assertArrayEquals(new double[][] {
                { 1, -1, -1 },
                { -6/5, 7/5, 4/5 },
                { -2/5, 4/5, 3/5 }
            }, invertedMatrix.getMatrixElements());
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testInverseWith5x5() {
        Matrix origMatrix = new Matrix(new double[][] {
            { 9, 6, 13, 7, 3  },
            { 14, 2, 8, 4, 10  },
            { 5, 11, 12, 15, 16 },
            { 17, 18, 19, 20, 21 },
            { 22, 23, 24, 25, 26 }
        });
        try {
            assertArrayEquals(new double[][] {
                { 0, -0.1429, 0.7143, 5.6, -4.0286 },
                { -0.0909, 0.2208, 1.013, -10.418, 7.7169 },
                { 0.0909, 0.2792, 0.987, -8.1818, 5.8831 },
                { 0.0909, -0.6494, -2.1558, 20.018, -14.603 },
                { -0.0909, 0.2922, 0.8701, -7.2182, 5.2312 }
            }, origMatrix.invert().getMatrixElements());
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            fail("Incorrectly threw a MatrixNotInvertibleException");
        }
    }

    @Test
    public void testDeterminant() {
        Matrix testMatrix = new Matrix(new double[][] { 
            { 4, 7 },
            { 2, 6 }
        });

        try {
            double determinant = testMatrix.getDeterminant();
            assertEquals(0.1, determinant, 0.0);
        } catch (MatrixNotInvertibleException e) {
            e.printStackTrace();
            fail();
        }
    }
}

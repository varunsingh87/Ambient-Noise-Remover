package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        assertEquals(new Matrix(new double[][] { { 25.0, 25.0, 25.0 }, { 25.0, 25.0, 25.0 }, { 25.0, 25.0, 25.0 } }), result);
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
}

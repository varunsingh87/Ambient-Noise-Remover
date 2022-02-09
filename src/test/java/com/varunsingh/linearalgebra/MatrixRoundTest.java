package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixRoundTest {
	@Test
	public void testRoundMatrix() {
		Matrix m = new Matrix(
			new double[][] { { 1.2345, 1.2345 }, { 1.2345, 1.2345 } }
		);
		Matrix expected = new Matrix(
			new double[][] { { 1.23, 1.23 }, { 1.23, 1.23 } }
		);
		assertEquals(expected, MatrixRound.roundMatrix(m, 2));
	}

	@Test
	public void testRoundMatrixTo0DecimalPlaces() {
		Matrix m = new Matrix(
			new double[][] { { 1.2345, 1.2345 }, { 1.2345, 1.2345 } }
		);
		Matrix expected = new Matrix(new double[][] { { 1, 1 }, { 1, 1 } });
		assertEquals(expected, MatrixRound.roundMatrix(m, 0));
	}
}

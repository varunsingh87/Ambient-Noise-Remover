package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertEquals;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.MatrixRound;
import com.varunsingh.linearalgebra.Vector;

import org.junit.Before;
import org.junit.Test;

public class KalmanFilterTest {
	private KalmanFilter algorithm;

	@Before
	public void initialize() {
		algorithm = new KalmanFilter(Vector.column(25, 6));
	}

	@Test
	public void testPredictState() {
		Dataset predictedState = algorithm.predictState(
			Vector.column(4000, 280)
		);
		assertEquals(Vector.column(4281, 282), predictedState);
	}

	@Test
	public void testInitialProcessCovariance() {
		Dataset initialProcessCovariance = algorithm.initialProcessCovariance(
			Vector.column(20, 5)
		);
		Matrix expected = new Matrix(new double[][] { { 400, 0 }, { 0, 25 } });
		assertEquals(expected, initialProcessCovariance);
	}

	@Test
	public void testPredictedProcessCovariance() {
		Dataset predictedProcessCovariance = algorithm.predictProcessCovariance(
			new Matrix(new double[][] { { 400, 0 }, { 0, 25 } })
		);
		Matrix expected = new Matrix(new double[][] { { 425, 0 }, { 0, 25 } });

		assertEquals(expected, predictedProcessCovariance);
	}

	@Test
	public void testKalmanGain() {
		Matrix actual = (Matrix) algorithm.calculateKalmanGain(
			new Matrix(new double[][] { { 425, 0 }, { 0, 25 } })
		);

		Matrix expected = new Matrix(
			new double[][] { { 0.405, 0 }, { 0, 0.410 } }
		);

		assertEquals(expected, MatrixRound.roundMatrix(actual, 3));
	}

	@Test
	public void testNewObservation() {
		Vector actual = (Vector) algorithm.calculateNewObservation(
			Vector.column(4260, 282)
		);
		Vector expected = Vector.column(4260, 282);

		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateState() {
		Vector expected = Vector.column(4272.495, 282);
		Vector actual = (Vector) algorithm.updateState(
			Vector.column(4281, 282), Vector.column(4260, 282), new Matrix(
				new double[][] { { 0.405, 0 }, { 0, 0.410 } }
			)
		);

		assertEquals(expected, actual);
	}

	@Test
	public void testUpdateProcessCovariance() {
		Matrix expected = new Matrix(
			new double[][] { { 252.9, 0 }, { 0, 14.8 } }
		);

		Dataset actual = MatrixRound.roundMatrix(
			(Matrix) algorithm.updateProcessCovariance(
				new Matrix(new double[][] { { 425, 0 }, { 0, 25 } }),
				new Matrix(new double[][] { { 0.405, 0 }, { 0, 0.410 } })
			), 1
		);

		assertEquals(expected, actual);
	}

	@Test
	public void testExecution() {
		Calculations firstIteration = algorithm.execute(
			new Calculations(
				Vector.column(4000, 280), // initial state
				(Matrix) algorithm.initialProcessCovariance(
					Vector.column(20, 5)
				)
			), Vector.column(4260, 282) // observation
		);

		var expectedPreviousCalculations = new Calculations(
			Vector.column(4272.5, 282.0), // predicted state
			new Matrix(new double[][] { { 252.977, 0 }, { 0, 14.754 } })
		);

		assertEquals(expectedPreviousCalculations, firstIteration.round());

		Calculations secondIteration = algorithm.execute(
			firstIteration, Vector.column(4550, 285) // observation
		);

		Calculations expectedSecondIterationCalculation = new Calculations(
			Vector.column(4553.851, 284.291), new Matrix(
				new double[][] { { 187.438, 0 }, { 0, 10.465 } }
			)
		);

		assertEquals(
			expectedSecondIterationCalculation, secondIteration.round()
		);

		Calculations thirdIteration = algorithm.execute(
			secondIteration, Vector.column(4860, 286) // observation
		);

		Calculations expectedThirdIterationCalculation = new Calculations(
			Vector.column(4844.158, 286.225), new Matrix(
				new double[][] { { 150.31, 0 }, { 0, 8.108 } }
			)
		);

		assertEquals(expectedThirdIterationCalculation, thirdIteration.round());
	}
}

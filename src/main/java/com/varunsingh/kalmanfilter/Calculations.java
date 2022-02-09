package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.MatrixRound;
import com.varunsingh.linearalgebra.Vector;

public record Calculations(Vector stateEstimate, Matrix processCovariance) {
	public String toString() {
		return String.format(
			"StateEstimate: %s \nProcessCovariance: %s", stateEstimate,
			processCovariance
		);
	}

	Calculations round() {
		return new Calculations(
			MatrixRound.roundVector(stateEstimate, 3), MatrixRound.roundMatrix(
				processCovariance, 3
			)
		);
	}
}

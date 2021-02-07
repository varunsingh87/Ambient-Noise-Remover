package com.varunsingh.kalmanfilter;

import java.util.Arrays;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class MultiDimensionalKalmanFilterEquationsFactoryTest {
    public void testStateExtrapolationEquation() {
        Vector stateVector = new Vector(10, 2, 1, 30, 30, 30);
        Vector controlVector = new Vector(5, 6, 7);

        Matrix result = new MultiDimensionalKalmanFilterEquationFactory(5).useStateExtrapolationEquation(stateVector, controlVector);
    }
}

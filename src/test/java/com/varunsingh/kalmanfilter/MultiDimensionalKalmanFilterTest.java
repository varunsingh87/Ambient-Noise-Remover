package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertArrayEquals;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

import org.junit.Test;

public class MultiDimensionalKalmanFilterTest {
    /**
     * Integration test on how the Propagation object interacts with the
     * MultiDimensionalKalmanFilter class
     */
    @Test
    public void testPropagation() {
        // Arrange
        double[] expectedStateElements = new double[] { 15.1, -9.8 };
        MultiDimensionalKalmanFilter.Builder mdkfBuilder = new MultiDimensionalKalmanFilter.Builder()
            .setTimeInterval(1.0);

        Propagation propagation = mdkfBuilder.createPropagation();
        
        propagation
            .setStateTransition(propagation.new StateTransition().POSITION_AND_VELOCITY)
            .setPreviousState(new Vector(new double[] { 20, 0 }))
            .setControl(propagation.new Control().ACCELERATION)
            .setInputVariable(new Matrix(-9.8).asRowVector());

        mdkfBuilder.setInitialState(propagation);

        MultiDimensionalKalmanFilter filter = new MultiDimensionalKalmanFilter(mdkfBuilder);

        // Act
        double[] outputStateMatrix = filter.predict().transpose().getMatrixElements()[0];

        assertArrayEquals(expectedStateElements, outputStateMatrix, 0.0);
    }
}

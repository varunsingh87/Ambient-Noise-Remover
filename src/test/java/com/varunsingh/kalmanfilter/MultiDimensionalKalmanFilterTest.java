package com.varunsingh.kalmanfilter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

import org.junit.Test;

public class MultiDimensionalKalmanFilterTest {
    /**
     * Integration test on how the Propagation object 
     * interacts with the MultiDimensionalKalmanFilter class
     * for "simple propagation," or propagation with just x, just y, or just z
     */
    @Test
    public void testSimplePropagation() {
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

    /**
     * Integration test on how the Propagation object 
     * interacts with the MultiDimensionalKalmanFilter class
     * for propagation with two of the following's position, velocity, and acceleration:
     * x, y, z
     */
    @Test
    public void testPropagationInTwoDimensions() {
        // Arrange
        double timeInterval = 1.0;
        //                                      x,  y,  xv, yv
        double[] previousState = new double[] { 20, -50, 3, 4 };
        double[] inputVariable = new double[] { 3, 0.1 };
        
        double[] expectedStateElements = new double[] { 
            previousState[0] + previousState[2] * timeInterval + inputVariable[0] * 0.5 * timeInterval * timeInterval,
            previousState[1] + previousState[3] * timeInterval + inputVariable[1] * 0.5 * timeInterval * timeInterval,
            previousState[2] + inputVariable[0] * timeInterval,
            previousState[3] + inputVariable[1] * timeInterval
        };

        Vector expectedState = new Vector(expectedStateElements);

        MultiDimensionalKalmanFilter.Builder mdkfBuilder = new MultiDimensionalKalmanFilter.Builder()
            .setTimeInterval(timeInterval);

        Propagation propagation = mdkfBuilder.createPropagation();

        // Act
        propagation
            .setStateTransition(propagation.new StateTransition().POSITION_AND_VELOCITY_TWO_DIMENSIONS)
            .setPreviousState(new Vector(previousState))
            .setControl(propagation.new Control().ACCELERATION_TWO_DIMENSIONS)
            .setInputVariable(new Vector(inputVariable));

        mdkfBuilder.setInitialState(propagation);

        MultiDimensionalKalmanFilter filter = new MultiDimensionalKalmanFilter(mdkfBuilder);
        Vector actualPredictedState = filter.predict();

        // Assert
        assertEquals(expectedState, actualPredictedState);
    }
}

package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

/**
 * A representation of the second equation of motion for constant acceleration
 * in matrix format
 * <p>
 * x = x0 + x't + 0.5x''t^2 or
 * </p>
 * x = x0 + v0t + 0.5at^2
 */
public class Propagation {
    /**
     * X
     */
    private Vector previousState;

    /**
     * The control variable matrix is the mathematical model that defines the state
     * vector's acceleration
     */
    private Matrix control;

    /**
     * The state transition matrix is the mathematical model that defines the state
     * vector's position and velocity
     */
    private Matrix stateTransition;

    /**
     * u
     */
    private Vector inputVariable;

    private Vector state;

    private double timeInterval;

    final class StateTransition {
        final Matrix POSITION_AND_VELOCITY = new Matrix(new double[][] { { 1, timeInterval }, { 0, 1 } });
        final Matrix POSITION_AND_VELOCITY_TWO_DIMENSIONS = new Matrix(new double[][] {
            { 1, 0, timeInterval, 0 },
            { 0, 1, 0,            timeInterval },
            { 0, 0, 1,            0 },
            { 0, 0, 0,            1 }
        });
    }

    final class Control {
        final double accel = 0.5 * timeInterval * timeInterval;
        final Matrix ACCELERATION = new Matrix(
            new double[][] { { accel }, { timeInterval } });
        final Matrix ACCELERATION_TWO_DIMENSIONS = new Matrix(
            new double[][] { { accel, 0 }, 
                             { 0, accel },
                             { timeInterval, 0 }, 
                             { 0, timeInterval } }
        );
    }

    public Propagation(double t) {
        timeInterval = t;
    }

    public Propagation setPreviousState(Vector x) {
        previousState = x;
        return this;
    }

    public Propagation setControl(Matrix g) {
        control = g;
        return this;
    }

    public Propagation setInputVariable(Vector u) {
        inputVariable = u;
        return this;
    }

    public Propagation setStateTransition(Matrix f) {
        stateTransition = f;
        return this;
    }

    /**
     * Calculates the state vector using the State Extrapolation Equation
     */
    public Vector predictNextStateVector() {
        Dataset positionAndVelocity = stateTransition.times(previousState);
        Dataset acceleration = control.times(inputVariable);
        return Vector.valueOf((Matrix) positionAndVelocity.plus(acceleration));
    }

    public Vector getState() {
        return state;
    }

    public Matrix getStateTransition() {
        return stateTransition;
    }

    public Vector getPreviousState() {
        return previousState;
    }
}

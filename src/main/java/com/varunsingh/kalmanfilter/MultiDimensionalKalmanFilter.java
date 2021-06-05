package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Vector;

public class MultiDimensionalKalmanFilter {
    private Propagation prediction;

    public MultiDimensionalKalmanFilter(Builder builder) {
        prediction = builder.initialState;  
    }

    Vector predict() {
        return prediction.predictNextStateVector();
    }

    public static class Builder {
        /**
         * Time between each cycle
         */
        private double timeInterval;

        private Propagation initialState;

        public Builder setTimeInterval(double ti) {
            timeInterval = ti;
            return this;
        }

        public Builder setInitialState(Propagation p) {
            initialState.setStateTransition(p.getStateTransition());
            initialState.setPreviousState(p.getPreviousState());
            
            return this;
        }

        public Propagation createPropagation() {
            initialState = new Propagation(timeInterval);
            return initialState;
        }
    }
}

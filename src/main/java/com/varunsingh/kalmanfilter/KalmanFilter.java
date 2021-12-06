package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Dataset;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

public class KalmanFilter {
    private double measurementError;
    private Calculations calculations;
    private int iteration;

    /**
     * The change in time between consecutive iterations
     */
    private double timeOfOneCycle = 1.0;

    private class Calculations {
        private double kalmanGain;
        private double estimate;
        private double errorEstimate;

        Calculations(double e) {
            estimate = e;
        }

        Calculations(double kg, double e, double ee) {
            kalmanGain = kg;
            estimate = e;
            errorEstimate = ee;
        }
    }

    public static void main(String[] args) {
        KalmanFilter kf = new KalmanFilter(68, 2, 75, 4);
        System.out.println(kf.calculations.estimate);
        kf.executeKalmanFilter(75);
        System.out.println(kf.calculations.kalmanGain);
        System.out.println(kf.calculations.estimate);
        System.out.println(kf.calculations.errorEstimate);
        kf.executeKalmanFilter(71);
        kf.executeKalmanFilter(70);
        kf.executeKalmanFilter(74);
        System.out.println(kf.calculations.estimate);
        System.out.println(kf.iteration);
    }

    KalmanFilter(double initialEstimate, double initialErrorEstimate, double initialMeasurement, double me) {
        measurementError = me;
        calculations = beginKalmanFilter(initialEstimate, initialErrorEstimate, initialMeasurement, me);
    }
    
    public Calculations beginKalmanFilter(double initialEstimate, double initialErrorEstimate, double initialMeasurement, double measurementError) {
        double firstKalmanGain = calculateKalmanGain(initialErrorEstimate);
        calculations = new Calculations(firstKalmanGain, initialEstimate, initialErrorEstimate);
        return recalculate(initialMeasurement);
    }

    void executeKalmanFilter(double measurement) {
        calculations = recalculate(measurement);
        iteration += 1;
    }

    Calculations recalculate(double measurement) {
        if (calculations.kalmanGain < 0.1) {
            return new Calculations(calculations.estimate);
        }

        // Calculate Kalman Gain
        double kalmanGain = calculateKalmanGain(calculations.errorEstimate);
        // Calculate estimate
        double currentEstimate = calculations.estimate + kalmanGain * (measurement - calculations.estimate);
        // Calculate error in the estimate
        double errorEstimate = (1 - kalmanGain) * calculations.errorEstimate;

        return new Calculations(kalmanGain, currentEstimate, errorEstimate);
    }

    private double calculateKalmanGain(double previousErrorEstimate) {
        return previousErrorEstimate / (previousErrorEstimate + measurementError);
    }

    Dataset predictState() {
        Matrix stateTransition = new Matrix(2, 2);
        stateTransition.setMatrixElements(new double[][] {
            { 1, timeOfOneCycle },
            { 0, 1 }
        });
        Vector previousState = new Vector(new double[] { 20, 0 });
        
        Matrix adaptiveControl = new Matrix(2, 2);
        Dataset controlVariable = new Matrix(0);
        
        return stateTransition.times(previousState).plus(adaptiveControl.times(controlVariable));
    }

    /**
     * Equation: Observation Matrix (C) * State Matrix (X) + Measurement Input Vector (z)
     * @param Vector measurementInput 
     * @param boolean velocity
     * @return
     */
    Dataset calculateMeasurement(Vector measurementInput, boolean velocity) {
        Vector previousState = new Vector(new double[] { 20, 0 });
        if (!velocity) {
            Matrix observation = new Matrix(new double[][] {{ 1, 0}});
            return observation.times(previousState);
        } else {
            Matrix observation = new Matrix(new double[][] {{ 1, 0}, { 0, 1 }});
            return observation.times(previousState);
        }
    }
}

package com.varunsingh.kalmanfilter;

public class KalmanFilter {
    private double measurementError;
    private Calculations calculations;
    private int time;

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
        System.out.println(kf.time);
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
        time += 1;
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
}

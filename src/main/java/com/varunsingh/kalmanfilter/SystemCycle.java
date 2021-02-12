package com.varunsingh.kalmanfilter;

import java.util.Objects;

/**
 * A model for the information in one cycle of a system
 */
public class SystemCycle {
    private double measurement;
    private double stateEstimate;
    private double stateVelocity;
    private double stateAcceleration;
    private double statePrediction;
    private double velocityPrediction;
    private double estimateUncertainty;
    private double measurementUncertainty;
    private double estimateUncertaintyPrediction;

    public SystemCycle() {
    }

    public SystemCycle(double m) {
        measurement = m;
    }

    public SystemCycle(double stateEst, double stateVel, double stateAcc) {
        stateEstimate = stateEst;
        stateVelocity = stateVel;
        stateAcceleration = stateAcc;
    }

    public SystemCycle(double m, double stateEst, double stateVel, double stateAcc, double statePred, double velPred) {
        measurement = m;
        stateEstimate = stateEst;
        stateVelocity = stateVel;
        stateAcceleration = stateAcc;
        statePrediction = statePred;
        velocityPrediction = velPred;
    }

    public SystemCycle(SystemCycle s) {
        measurement = s.getMeasurement();
        stateEstimate = s.getStateEstimate();
        stateVelocity = s.getStateVelocity();
        stateAcceleration = s.getStateAcceleration();
        statePrediction = s.getStatePrediction();
        velocityPrediction = s.getVelocityPrediction();
        estimateUncertainty = s.getEstimateUncertainty();
        measurementUncertainty = s.getMeasurementUncertainty();
        estimateUncertaintyPrediction = s.getEstimateUncertaintyPrediction();
    }

    public double getMeasurement() {
        return measurement;
    }

    void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public double getStateEstimate() {
        return stateEstimate;
    }

    public void setStateEstimate(double stateEstimate) {
        this.stateEstimate = stateEstimate;
    }

    public double getStateVelocity() {
        return stateVelocity;
    }

    void setStateVelocity(double stateVelocity) {
        this.stateVelocity = stateVelocity;
    }

    public double getStateAcceleration() {
        return stateAcceleration;
    }

    void setStateAcceleration(double stateAcceleration) {
        this.stateAcceleration = stateAcceleration;
    }

    public double getStatePrediction() {
        return statePrediction;
    }

    void setStatePrediction(double statePrediction) {
        this.statePrediction = statePrediction;
    }

    public double getVelocityPrediction() {
        return velocityPrediction;
    }

    void setVelocityPrediction(double velocityPrediction) {
        this.velocityPrediction = velocityPrediction;
    }

    public double getMeasurementUncertainty() {
        return measurementUncertainty;
    }

    public void setMeasurementUncertainty(double measurementUncertainty) {
        this.measurementUncertainty = measurementUncertainty;
    }

    public double getEstimateUncertainty() {
        return estimateUncertainty;
    }

    public void setEstimateUncertainty(double estimateUncertainty) {
        this.estimateUncertainty = estimateUncertainty;
    }

    public double getEstimateUncertaintyPrediction() {
        return estimateUncertaintyPrediction;
    }

    public void setEstimateUncertaintyPrediction(double estimateUncertaintyPrediction) {
        this.estimateUncertaintyPrediction = estimateUncertaintyPrediction;
    }

    @Override
    public String toString() {
        String str = "\t ------System State------ \n";
        
        str += conditionalIncludeStr("Measurement", measurement);
        str += conditionalIncludeStr("State Estimate", stateEstimate);
        str += conditionalIncludeStr("State Velocity", stateVelocity);
        str += conditionalIncludeStr("State Acceleration", stateAcceleration);
        str += conditionalIncludeStr("State Prediction", statePrediction);
        str += conditionalIncludeStr("Velocity Prediction", velocityPrediction);

        return str;
    }

    private String conditionalIncludeStr(String propName, double propValue) {
        if (Objects.isNull(propValue) || propValue == 0) return "";
        else return toStringProperty(propName, propValue);
    }

    private String toStringProperty(String propName, double propValue) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        return String.format("%s%s %s%s\n", ANSI_YELLOW, propName, ANSI_RESET, propValue);
    }

}

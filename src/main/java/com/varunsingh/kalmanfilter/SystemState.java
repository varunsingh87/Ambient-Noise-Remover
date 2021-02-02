package com.varunsingh.kalmanfilter;

import java.util.Objects;

/**
 * A model for the information in one cycle of a system
 */
public class SystemState {
    private double measurement;
    private double stateEstimate;
    private double stateVelocity = 0;
    private double stateAcceleration = 0;
    private double statePrediction;
    private double velocityPrediction = 0;

    public SystemState(double m) {
        measurement = m;
    }

    public SystemState(double m, double stateEst, double statePred) {
        measurement = m;
        stateEstimate = stateEst;
        statePrediction = statePred;
    }

    public SystemState(double m, double stateEst, double stateVel, double stateAcc, double statePred, double velPred) {
        measurement = m;
        stateEstimate = stateEst;
        stateVelocity = stateVel;
        stateAcceleration = stateAcc;
        statePrediction = statePred;
        velocityPrediction = velPred;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
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

    public void setStateVelocity(double stateVelocity) {
        this.stateVelocity = stateVelocity;
    }

    public double getStateAcceleration() {
        return stateAcceleration;
    }

    public void setStateAcceleration(double stateAcceleration) {
        this.stateAcceleration = stateAcceleration;
    }

    public double getStatePrediction() {
        return statePrediction;
    }

    public void setStatePrediction(double statePrediction) {
        this.statePrediction = statePrediction;
    }

    public double getVelocityPrediction() {
        return velocityPrediction;
    }

    public void setVelocityPrediction(double velocityPrediction) {
        this.velocityPrediction = velocityPrediction;
    }

    @Override
    public String toString() {
        String str = "\t ------System State------ \n";
        str += toStringProperty("Measurement", measurement);
        
        if (!Objects.isNull(stateEstimate)) str += toStringProperty("State Estimate", stateEstimate);

        if (!Objects.isNull(stateVelocity)) str += toStringProperty("State Velocity", stateVelocity);

        return str;
    }

    private String toStringProperty(String propName, double propValue) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        return ANSI_YELLOW + propName + " " + ANSI_RESET + propValue + "\n";
    }

}

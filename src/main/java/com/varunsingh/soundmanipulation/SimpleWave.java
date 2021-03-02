package com.varunsingh.soundmanipulation;

import java.util.Objects;

public class SimpleWave {
    private double frequency;
    private double amplitude;
    private double seconds;

    public SimpleWave(double f, double a) {
        setFrequency(f);
        setAmplitude(a);
        seconds = Double.POSITIVE_INFINITY;
    }

    public SimpleWave(double f, double a, double t) {
        setFrequency(f);
        setAmplitude(a);
        setSeconds(t);
    }

    public double getFrequency() {
        return frequency;
    }

    public double getFrequencyInRadians() {
        return 2 * Math.PI * frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public boolean hasFiniteTimeValue() {
        return Double.isFinite(seconds);
    }

    public double getSeconds() {
        return seconds;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public float calcSample(double time) {
        return (float) (amplitude * Math.sin(getFrequencyInRadians() * time));
    }

    public SimpleWave invert() {
        return new SimpleWave(
            frequency,
            -amplitude
        );
    }

    public CompoundWave append(SimpleWave waveToConcat) {
        return new CompoundWave(this, waveToConcat);        
    }
}

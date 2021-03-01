package com.varunsingh.ambientnoiseremover;

public class SimpleWave {
    private double frequency;
    private double amplitude;

    public SimpleWave(double f, double a) {
        setFrequency(f);
        setAmplitude(a);
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

    public float calcSample(double time) {
        return (float) (amplitude * Math.sin(getFrequencyInRadians() * time));
    }

    public SimpleWave add(SimpleWave addendWave) {
        return new SimpleWave(
            frequency + addendWave.getFrequency(),
            amplitude + addendWave.getAmplitude()
        );
    }

    public CompoundWave append(SimpleWave waveToConcat) {
        throw new UnsupportedOperationException();        
    }
}

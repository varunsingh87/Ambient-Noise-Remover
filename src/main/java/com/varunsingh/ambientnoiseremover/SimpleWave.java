package com.varunsingh.ambientnoiseremover;

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
		return (float) (amplitude * Math.sin(2 * Math.PI * frequency * time));
	}

	public SimpleWave invert() {
		return new SimpleWave(frequency, -amplitude);
	}

	public int getSampleCount(float sampleRate) {
		return (int) (getSeconds() * sampleRate);
	}

	public Float[] samples(float sampleRate) {
		Float[] buffer = new Float[getSampleCount(sampleRate)];

		for (int sample = 0; sample < buffer.length; sample++) {
			double time = sample / sampleRate;
			buffer[sample] = calcSample(time);
		}

		return buffer;
	}
}

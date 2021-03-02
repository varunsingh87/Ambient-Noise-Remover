package com.varunsingh.soundmanipulation;

import java.util.Arrays;

/**
 * A representation of any sound wave
 */
public class CompoundWave implements Wave {
    private byte[] audioData;
    private byte maximum;
    private byte minimum;
    private double seconds;

    public CompoundWave(byte[] data) {
        audioData = data;
        minimum = findMinimum();
        maximum = findMaximum();
    }

    public CompoundWave(SimpleWave... appendingWaves) {
        seconds = findSumOfSeconds(appendingWaves);        
	}

    protected double findSumOfSeconds(SimpleWave... appendingWaves) {
        double sum = 0;
        
        for (int i = 0; i < appendingWaves.length; i++) {
            sum += appendingWaves[i].getSeconds();
        }

        return sum;
    }

	private byte findMinimum() {
        byte currentMin = Byte.MAX_VALUE;
        for (int i = 0; i < audioData.length; i++) {
            if (audioData[i] < currentMin) {
                currentMin = audioData[i];
            }
        }
        return currentMin;
    }

    public byte getMinimum() {
        return minimum;
    }

    private byte findMaximum() {
        byte currentMax = Byte.MIN_VALUE;
        for (int i = 0; i < audioData.length; i++) {
            if (audioData[i] > currentMax) {
                currentMax = audioData[i];
            }
        }
        return currentMax;
    }

    public byte getMaximum() {
        return maximum;
    }

    public byte[] getAudioData() {
        return audioData;
    }

    public double getSeconds() {
        return seconds;
    }

    public boolean equals(Object object) {
        return audioData.equals(((CompoundWave) object).getAudioData());
    }

    @Override
    public CompoundWave add(Wave wave) {
        return (CompoundWave) wave.add(this);
    }

    CompoundWave mute() {
        return add(invert());
    }

    @Override
    public CompoundWave invert() {
        byte[] invertedAudioData = new byte[audioData.length];

        for (int i = 136; i < invertedAudioData.length; i++) {
            invertedAudioData[i] = (byte) (-1 * ((short) audioData[i]));
        }

        return new CompoundWave(invertedAudioData);
    }

    public CompoundWave invert(byte noise) {
        byte[] invertedAudioData = new byte[audioData.length];

        for (int i = 0; i < invertedAudioData.length; i++) {
            invertedAudioData[i] -= noise;
        }

        return new CompoundWave(invertedAudioData);
    }

    public CompoundWave invert(int start, int end) {
        byte[] invertedAudioData = new byte[audioData.length];

        for (int i = start; i < end; i++) {
            invertedAudioData[i] = (byte) (-1 * ((short) audioData[i]));
        }

        return new CompoundWave(invertedAudioData);
    }

    public CompoundWave invert(int start, int end, byte noise) {
        byte[] invertedAudioData = new byte[audioData.length];
        
        for (int i = start; i < end; i++) {
            invertedAudioData[i] -= noise;
        }

        return new CompoundWave(invertedAudioData);
    }
}

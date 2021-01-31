package com.varunsingh.ambientnoiseremover;

/**
 * A representation of any sound wave
 */
public class WaveForm {
    private byte[] audioData;
    private byte maximum;
    private byte minimum;

    public WaveForm(byte[] data) {
        audioData = data;
        minimum = findMinimum();
        maximum = findMaximum();
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

    public void setAudioData(byte[] newData) {
        audioData = newData;
    }

    public boolean equals(Object object) {
        return audioData.equals(((WaveForm) object).getAudioData());
    }

    public WaveForm add(WaveForm wave) {
        byte[] sumByteData = new byte[Math.max(wave.getAudioData().length, getAudioData().length)];

        for (int i = 0; i < wave.getAudioData().length; i++) {
            sumByteData[i] = (byte) (audioData[i] + wave.getAudioData()[i]);
        }

        return new WaveForm(sumByteData);
    }

    WaveForm mute() {
        return add(invert());
    }

    public WaveForm invert() {
        byte[] invertedAudioData = new byte[audioData.length];

        for (int i = 136; i < invertedAudioData.length; i++) {
            invertedAudioData[i] = (byte) (-1 * ((short) audioData[i]));
        }

        return new WaveForm(invertedAudioData);
    }
}

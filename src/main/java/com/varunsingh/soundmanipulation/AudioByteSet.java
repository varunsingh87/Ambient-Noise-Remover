package com.varunsingh.soundmanipulation;

import be.tarsos.dsp.beatroot.Agent;

public class AudioByteSet {
    private byte[] byteBuffer;
    private Agent agent;

    public AudioByteSet(byte[] bb) {
        byteBuffer = bb;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * Converts a sample buffer of float's to a byte array buffer
     * 
     * @implNote Code by Joshua Beckford on StackOverflow
     * @see https://stackoverflow.com/a/28934588/9860982
     * @param buffer The buffer of samples
     * @return A byte array representing the samples as data in WAV format
     */
    public static AudioByteSet createByteBufferFromSampleBuffer(float[] buffer) {
        final byte[] byteBuffer = new byte[buffer.length * 2];

        for (int i = 0; i < buffer.length; i++) {
            final int x = (int) (buffer[i] * (Math.pow(2, 15) - 1));
           
            byteBuffer[i*2] = (byte) x;
            byteBuffer[i*2 + 1] = (byte) (x >>> 8);
        }

        return new AudioByteSet(byteBuffer);
    }
}

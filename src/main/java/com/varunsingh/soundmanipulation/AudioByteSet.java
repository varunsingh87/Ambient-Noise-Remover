package com.varunsingh.soundmanipulation;

public class AudioByteSet {
    private byte[] byteBuffer;

    public AudioByteSet(float[] buffer) {
        setByteBuffer(createByteBufferFromSampleBuffer(buffer));
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
    public static byte[] createByteBufferFromSampleBuffer(float[] buffer) {
        final byte[] byteBuffer = new byte[buffer.length * 2];

        int bufferIndex = 0;
        for (int i = 0; i < byteBuffer.length; i++) {
            final int x = (int)(buffer[bufferIndex++] * 32767.0);

            byteBuffer[i++] = (byte)x;
            byteBuffer[i] = (byte)(x >>> 8);
        }

        return byteBuffer;
    }
}

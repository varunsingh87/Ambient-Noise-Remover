package com.varunsingh.ambientnoiseremover;

import java.io.File;

import javax.sound.sampled.AudioFormat;

public class SoundSandbox {
    static double sampleRate = 44100.0;
    static double seconds = 2d;

    public static void main(String[] args) {
        recordSimpleWave();
        recordTwoSimpleWaves();
    }
    
    private static CompoundWave combineWaves() {
        SimpleWave firstWave = new SimpleWave(440.0, 0.8);

        SimpleWave secondWave = new SimpleWave(6 * firstWave.getFrequency(), 0.2);

        float[] buffer = new float[(int) (seconds * sampleRate)];

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            
        }

        return new CompoundWave(new byte[] {});
    } 

    private static SimpleWave createSimpleWave() {
        return new SimpleWave(440.0, 0.8);
    }

    /**
     * 
     * @param wave
     * @return
     */
    private static float[] createSampleBufferFromWave(SimpleWave wave) {
        float[] buffer = new float[(int) (seconds * sampleRate)];
        
        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            buffer[sample] = wave.calcSample(time);
        }

        return buffer;
    }

    /**
     * 
     * 
     * Code by Joshua Beckford on StackOverflow
     * @see https://stackoverflow.com/a/28934588/9860982
     * @param buffer
     * @return A byte array of 
     */
    private static byte[] createByteBufferFromSampleBuffer(float[] buffer) {
        final byte[] byteBuffer = new byte[buffer.length * 2];

        int bufferIndex = 0;
        for (int i = 0; i < byteBuffer.length; i++) {
            final int x = (int)(buffer[bufferIndex++] * 32767.0);

            byteBuffer[i++] = (byte)x;
            byteBuffer[i] = (byte)(x >>> 8);
        }

        return byteBuffer;
    }

    private static AudioFormat createArbitraryAudioFormat() {
        final boolean isBigEndian = false;
        final boolean signed = true;

        final int bits = 16;
        final int channels = 1;

        return new AudioFormat((float) sampleRate, bits, channels, signed, isBigEndian);
    }

    private static void recordSimpleWave() {
        AudioFormat sampleFormat = createArbitraryAudioFormat();
        SimpleWave sampleWave = createSimpleWave();
        float[] sampleBuffer = createSampleBufferFromWave(sampleWave);
        byte[] byteBuffer = createByteBufferFromSampleBuffer(sampleBuffer);

        File out = new File("data/sandbox/outputs/one_simple.wav");

        AudioFinalizer finalizer = new AudioFinalizer(sampleFormat, byteBuffer, sampleBuffer.length);
        finalizer.writeToOutputFile(out);
    }

    private static void recordTwoSimpleWaves() {
        AudioFormat sampleFormat = createArbitraryAudioFormat();
        
        SimpleWave firstSimpleWave = createSimpleWave();
        float[] firstSampleBuffer = createSampleBufferFromWave(firstSimpleWave);
        
        SimpleWave secondSimpleWave = new SimpleWave(460.0, 0.7);
        float[] secondSampleBuffer = createSampleBufferFromWave(secondSimpleWave);
        
        float[] combinedSample = new float[firstSampleBuffer.length + secondSampleBuffer.length];
        System.arraycopy(firstSampleBuffer, 0, combinedSample, 0, firstSampleBuffer.length);
        System.arraycopy(secondSampleBuffer, 0, combinedSample, firstSampleBuffer.length, secondSampleBuffer.length);
        
        byte[] buffer = createByteBufferFromSampleBuffer(combinedSample);
        File out = new File("data/sandbox/outputs/two_sideBySide.wav");
        AudioFinalizer finalizer = new AudioFinalizer(sampleFormat, buffer, combinedSample.length);
        finalizer.writeToOutputFile(out);
    }
}

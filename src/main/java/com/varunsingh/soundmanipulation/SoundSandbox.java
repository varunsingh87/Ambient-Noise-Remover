package com.varunsingh.soundmanipulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;

public class SoundSandbox {
    static double sampleRate = 44100.0;
    static double seconds = 2d;

    public static void main(String[] args) {
        recordSilence();
        recordCombination();
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
     * Converts a sample buffer of float's to a byte array buffer
     * @implNote Code by Joshua Beckford on StackOverflow
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

    private static void concatTwoWaves() {
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

    private static void concatNWaves(SimpleWave... waves) {
        AudioFormat sampleFormat = createArbitraryAudioFormat();
        
        List<Float> combinedSampleBuffer = new ArrayList<Float>();

        for (SimpleWave wave : waves) {
            float[] waveSampleBuffer = createSampleBufferFromWave(wave);

            for (int i = 0; i < waveSampleBuffer.length; i++) {
                combinedSampleBuffer.add(waveSampleBuffer[i]);
            }
        }

        float[] combinedSampleBufferArr = new float[combinedSampleBuffer.size()];
        for (int i = 0; i < combinedSampleBuffer.size(); i++) {
            float f = combinedSampleBuffer.get(i);
            combinedSampleBufferArr[i] = Objects.isNull(f) ? Float.NaN : f;
        }

        byte[] byteBuffer = createByteBufferFromSampleBuffer(combinedSampleBufferArr);
        File out = new File("data/sandbox/outputs/five_noteScale.wav");
        AudioFinalizer finalizer = new AudioFinalizer(sampleFormat, byteBuffer, combinedSampleBuffer.size());
        finalizer.writeToOutputFile(out);
    }

    private static void recordSilence() {
        AudioFormat format = createArbitraryAudioFormat();
        SimpleWave wave = new SimpleWave(440, 0.5);
        float[] wave1Samples = createSampleBufferFromWave(wave);
        float[] inverseSamples = createSampleBufferFromWave(wave.invert());

        float[] newSamples = new float[wave1Samples.length];
        for (int i = 0; i < wave1Samples.length; i++) {
            newSamples[i] = wave1Samples[i] + inverseSamples[i];
        }

        byte[] silenceByteBuffer = createByteBufferFromSampleBuffer(newSamples);
        File out = new File("data/sandbox/outputs/silence.wav");
        AudioFinalizer finalizer = new AudioFinalizer(format, silenceByteBuffer, newSamples.length);
        finalizer.writeToOutputFile(out);
    }

    private static void recordCombination() {
        AudioFormat format = createArbitraryAudioFormat();
        float[] wave1Samples = createSampleBufferFromWave(new SimpleWave(420, 0.5));
        float[] inverseSamples = createSampleBufferFromWave(new SimpleWave(480, 0.8));

        float[] newSamples = new float[wave1Samples.length];
        for (int i = 0; i < wave1Samples.length; i++) {
            newSamples[i] = wave1Samples[i] + inverseSamples[i];
        }

        byte[] silenceByteBuffer = createByteBufferFromSampleBuffer(newSamples);
        File out = new File("data/sandbox/outputs/combination.wav");
        AudioFinalizer finalizer = new AudioFinalizer(format, silenceByteBuffer, newSamples.length);
        finalizer.writeToOutputFile(out);
    }
}

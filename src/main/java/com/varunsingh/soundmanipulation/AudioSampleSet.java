package com.varunsingh.soundmanipulation;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;

public class AudioSampleSet {
    static double sampleRate = 44100.0;
    static double seconds = 2d;

    float[] sampleBuffer;

    public AudioSampleSet(SimpleWave simpleWave) {
        sampleBuffer = createSampleBufferFromWave(simpleWave);
    }

    public AudioSampleSet(float[] sb) {
        sampleBuffer = sb;
    }

    public AudioSampleSet(byte[] bb) {
        ShortBuffer sbuf = ByteBuffer.wrap(bb).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        short[] audioShorts = new short[sbuf.capacity()];
        sbuf.get(audioShorts);
    }

    public float[] getSampleBuffer() {
        return sampleBuffer;
    }

    public void setSampleBuffer(float[] newSampleBuffer) {
        sampleBuffer = newSampleBuffer;
    }

    /**
     * Converts a SimpleWave object into a float[] of samples representing the
     * buffer
     * 
     * @param wave The SimpleWave object representing the waveform
     * @return The sample buffer
     */
    public static float[] createSampleBufferFromWave(SimpleWave wave) {
        float[] buffer = new float[(int) (seconds * sampleRate)];

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            buffer[sample] = wave.calcSample(time);
        }

        return buffer;
    }

    public static AudioFormat createArbitraryAudioFormat() {
        final boolean isBigEndian = false;
        final boolean signed = true;

        final int bits = 16;
        final int channels = 1;

        return new AudioFormat((float) sampleRate, bits, channels, signed, isBigEndian);
    }

    public AudioSampleSet concat(float[] secondSampleBuffer) {
        SimpleWave firstSimpleWave = new SimpleWave(440.0, 0.8);
        float[] firstSampleBuffer = AudioSampleSet.createSampleBufferFromWave(firstSimpleWave);

        float[] combinedSample = new float[firstSampleBuffer.length + secondSampleBuffer.length];

        System.arraycopy(firstSampleBuffer, 0, combinedSample, 0, firstSampleBuffer.length);
        System.arraycopy(secondSampleBuffer, 0, combinedSample, firstSampleBuffer.length, secondSampleBuffer.length);

        return new AudioSampleSet(combinedSample);
    }

    public AudioSampleSet concatNWaves(SimpleWave... waves) {
        List<Float> combinedSampleBuffer = new ArrayList<Float>();

        for (SimpleWave wave : waves) {
            float[] waveSampleBuffer = AudioSampleSet.createSampleBufferFromWave(wave);

            for (int i = 0; i < waveSampleBuffer.length; i++) {
                combinedSampleBuffer.add(waveSampleBuffer[i]);
            }
        }

        float[] combinedSampleBufferArr = new float[combinedSampleBuffer.size()];
        for (int i = 0; i < combinedSampleBuffer.size(); i++) {
            float f = combinedSampleBuffer.get(i);
            combinedSampleBufferArr[i] = Objects.isNull(f) ? Float.NaN : f;
        }

        return new AudioSampleSet(combinedSampleBufferArr);
    }

    public AudioSampleSet add(float[] addendSampleBuffer) {
        float[] wave1Samples = AudioSampleSet.createSampleBufferFromWave(new SimpleWave(420, 0.5));

        float[] newSamples = new float[Math.max(wave1Samples.length, addendSampleBuffer.length)];

        for (int i = 0; i < wave1Samples.length; i++) {
            newSamples[i] = wave1Samples[i] + addendSampleBuffer[i];
        }

        return new AudioSampleSet(newSamples);
    }

    public static void recordSilence() {
        AudioFormat format = AudioSampleSet.createArbitraryAudioFormat();
        SimpleWave wave = new SimpleWave(440, 0.5);
        float[] wave1Samples = AudioSampleSet.createSampleBufferFromWave(wave);
        float[] inverseSamples = AudioSampleSet.createSampleBufferFromWave(wave.invert());

        float[] newSamples = new float[wave1Samples.length];
        for (int i = 0; i < wave1Samples.length; i++) {
            newSamples[i] = wave1Samples[i] + inverseSamples[i];
        }

        byte[] silenceByteBuffer = AudioByteSet.createByteBufferFromSampleBuffer(newSamples);
        File out = new File("data/sandbox/outputs/silence.wav");
        AudioFileManager.writeToOutputFile(format, silenceByteBuffer, newSamples.length, out);
    }
}

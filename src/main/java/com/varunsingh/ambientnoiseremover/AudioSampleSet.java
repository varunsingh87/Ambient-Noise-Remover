package com.varunsingh.ambientnoiseremover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AudioSampleSet {
    static double sampleRate = 44100.0;
    static double seconds = 2d;

    float[] sampleBuffer;

    public AudioSampleSet(float[] sb) {
        sampleBuffer = sb;
    }

    public float[] getSampleBuffer() {
        return sampleBuffer;
    }

    public void setSampleBuffer(float[] newSampleBuffer) {
        sampleBuffer = newSampleBuffer;
    }

    public int getBufferSize() {
        return sampleBuffer.length;
    }

    public float get(int index) {
        return sampleBuffer[index];
    }

    public void set(int index, float newSampleVal) {
        sampleBuffer[index] = newSampleVal;
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(
            ((AudioSampleSet) obj).getSampleBuffer(), sampleBuffer
        );
    }

    @Override
    public String toString() {
        return "Sample Buffer " + sampleBuffer.toString() + " with "
            + getBufferSize() + " samples";
    }

    /**
     * Factory method to convert a SimpleWave object into a float[] of samples
     * representing the buffer
     * 
     * @param   wave The SimpleWave object representing the waveform
     * @return       The sample buffer
     * @apiNote      Encapsulates the float[] inside the AudioSampleSet object
     */
    public static AudioSampleSet createSampleBufferFromWave(SimpleWave wave) {
        double bufferSeconds = wave.hasFiniteTimeValue() ? wave.getSeconds()
            : seconds;
        float[] buffer = new float[(int) (bufferSeconds * sampleRate)];

        for (int sample = 0; sample < buffer.length; sample++) {
            double time = sample / sampleRate;
            buffer[sample] = wave.calcSample(time);
        }

        return new AudioSampleSet(buffer);
    }

    public AudioSampleSet concat(AudioSampleSet secondSampleBuffer) {
        SimpleWave firstSimpleWave = new SimpleWave(440.0, 0.8);
        AudioSampleSet firstSampleBuffer = AudioSampleSet
            .createSampleBufferFromWave(firstSimpleWave);

        AudioSampleSet combinedSample = new AudioSampleSet(
            new float[firstSampleBuffer.getBufferSize() + secondSampleBuffer
                .getBufferSize()]
        );

        System.arraycopy(
            firstSampleBuffer, 0, combinedSample, 0, firstSampleBuffer
                .getBufferSize()
        );
        System.arraycopy(
            secondSampleBuffer, 0, combinedSample, firstSampleBuffer
                .getBufferSize(), secondSampleBuffer.getBufferSize()
        );

        return combinedSample;
    }

    public AudioSampleSet concatNWaves(SimpleWave... waves) {
        List<Float> combinedSampleBuffer = new ArrayList<Float>();

        for (SimpleWave wave : waves) {
            AudioSampleSet waveSampleBuffer = AudioSampleSet
                .createSampleBufferFromWave(wave);

            for (int i = 0; i < waveSampleBuffer.getBufferSize(); i++) {
                combinedSampleBuffer.add(waveSampleBuffer.get(i));
            }
        }

        float[] combinedSampleBufferArr = new float[combinedSampleBuffer
            .size()];
        for (int i = 0; i < combinedSampleBuffer.size(); i++) {
            float f = combinedSampleBuffer.get(i);
            combinedSampleBufferArr[i] = Objects.isNull(f) ? Float.NaN : f;
        }

        return new AudioSampleSet(combinedSampleBufferArr);
    }

    public AudioSampleSet add(AudioSampleSet addendSampleBuffer) {
        AudioSampleSet wave1Samples = AudioSampleSet.createSampleBufferFromWave(
            new SimpleWave(420, 0.5)
        );

        float[] newSamples = new float[Math.max(
            wave1Samples.getSampleBuffer().length, addendSampleBuffer
                .getSampleBuffer().length
        )];

        for (int i = 0; i < wave1Samples.getSampleBuffer().length; i++) {
            newSamples[i] = wave1Samples.getSampleBuffer()[i]
                + addendSampleBuffer.getSampleBuffer()[i];
        }

        return new AudioSampleSet(newSamples);
    }

    public AudioSampleSet muteNoise(float noise) {
        AudioSampleSet noiseMuted = new AudioSampleSet(
            new float[getBufferSize()]
        );

        for (int i = 0; i < getBufferSize(); i++) {
            noiseMuted.set(i, get(i) - noise);
        }

        return noiseMuted;
    }

    public AudioSampleSet muteNoise(double[] noises) {
        AudioSampleSet noiseMuted = new AudioSampleSet(
            new float[getBufferSize()]
        );

        for (int i = 0, j = 1; i < getBufferSize()
            && j <= noises.length; i++, j++) {
            noiseMuted.set(i, get(i) - (float) noises[i % j]);
        }

        return noiseMuted;
    }
}

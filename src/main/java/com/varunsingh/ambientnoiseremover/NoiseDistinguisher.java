package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.varunsingh.soundmanipulation.AudioSampleSet;

public class NoiseDistinguisher {
    public final static float NOISE_THRESHOLD = 0.815f;

    private AudioSampleSet sampleBuffer;

    NoiseDistinguisher(AudioSampleSet b) {
        sampleBuffer = b;
    }

    NoiseDistinguisher(File f) {
        try {
            sampleBuffer = AudioSampleSet.createSampleBufferFromByteBuffer(AudioSystem.getAudioInputStream(f).readAllBytes());
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the samples below the {@code NOISE_THRESHOLD}
     * @return A map of the positions in the byte array to their calculates amplitude
     * or an empty hash map if an exception occurred
     * @implNote Uses code by Radiodef on StackOverflow
     * @see https://stackoverflow.com/a/26576548/9860982
     */
    public List<Float> findSamplesBelowNoiseThreshold() {
        List<Float> pauseValues = new ArrayList<>();
                
        for (int i = 0; i < sampleBuffer.getBufferSize(); i++) {
            float sample = sampleBuffer.get(i);

            if (sample < NOISE_THRESHOLD) {
                pauseValues.add(sample);
            }
        }

        return pauseValues;
    }
}

package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class NoiseDistinguisher {
    public final static float NOISE_THRESHOLD = 0.115f;

    private File sourceFile;

    NoiseDistinguisher(File src) {
        sourceFile = src;
    }

    /**
     * Finds the samples below the {@code NOISE_THRESHOLD}
     * @return A map of the positions in the byte array to their calculates amplitude
     * or an empty hash map if an exception occurred
     * @implNote Uses code by Radiodef on StackOverflow
     * @see https://stackoverflow.com/a/26576548/9860982
     */
    public HashMap<Integer, Float> findSamplesBelowNoiseThreshold() {
        try (AudioInputStream noisySoundStream = AudioSystem.getAudioInputStream(sourceFile)) {
            try (Clip clip = AudioSystem.getClip()) {
                HashMap<Integer, Float> noisePositions = new HashMap<Integer, Float>();

                byte[] buf = new byte[clip.getBufferSize()];
                float[] samples = new float[buf.length / 2];

                int bytesRead;
                while ((bytesRead = noisySoundStream.read(buf, 0, buf.length)) != -1) {

                    // convert bytes to samples here
                    for (int i = 0, s = 0; i < bytesRead;) {
                        int sample = 0;

                        sample |= buf[i++] & 0xFF; // (reverse these two lines
                        sample |= buf[i++] << 8; // if the format is big endian)

                        // normalize to range of +/-1.0f
                        samples[s++] = sample / 32768f;
                    }

                    float rms = 0f;
                    for (float sample : samples) {
                        rms += sample * sample;
                    }

                    rms = (float) Math.sqrt(rms / samples.length);

                    if (rms < NOISE_THRESHOLD)
                        noisePositions.put(bytesRead, rms);
                }

                return noisePositions;
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashMap<Integer, Float>();
    }
}

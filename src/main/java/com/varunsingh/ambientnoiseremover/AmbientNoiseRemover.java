package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.varunsingh.kalmanfilter.Calculations;
import com.varunsingh.kalmanfilter.KalmanFilter;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;

import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FrequencyBin;

/*
 * Ambient Noise Remover: A program that removes background noise from an audio
 * file using the multidimensional Kalman Filter
 * Copyright (C) 2022 Varun Singh
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/**
 * Samples: Talk by Neil Cummings at Kunsthal Aarhus, on 11 December, as part of
 * The Perfect Institution series. Source: FreeSound, user kunsthalaarhus
 * {@link https://freesound.org/people/kunsthalaarhus/sounds/211081/}
 * 
 * @author  @varunsingh87 Varun Singh
 * @apiNote Has a constructor and a main method for testing multiple samples at
 *          once
 */
public class AmbientNoiseRemover {
    private SoundManager sm;
    private File sourceAudio;
    private File outputAudio;

    public AmbientNoiseRemover(File src, File dest)
        throws UnsupportedAudioFileException, IOException {
        sm = new SoundManager(AudioSystem.getAudioInputStream(src));
        sourceAudio = src;
        outputAudio = dest;
    }

    public static void main(String[] args) {
        removeAmbientNoiseFromFiles();
    }

    double applyHammingWindow(int n, int frameDuration) {
        return 0.54 - 0.46 * Math.cos(2 * Math.PI * n / (frameDuration - 1));
    }

    void useHighPassFilter(double frequency, int rollOff) {

    }

    void useLowPassFilter() {
        try {
            FFTResult entireAudio = new QuiFFT(sourceAudio).fullFFT();

            for (FFTFrame f : entireAudio.fftFrames) {
                for (FrequencyBin b : f.bins) {
                    if (b.frequency < 1000) {
                        b.amplitude = 0;
                    }
                }
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    void useKalmanFilter() {
        try {
            Float[] sampleBuffer = sm.loadSamplesFromWav();
            System.out.println(
                "Removing noise from audio sample " + sourceAudio.getName()
                    + " of length " + sampleBuffer.length + "..."
            );

            // Too high (> 1) measurement noise = Extreemely fluctuating,
            // nearly inaudible
            // Too low (< 0.5) square block
            // 0.6 < optimal values < 1.0
            KalmanFilter kf = new KalmanFilter(
                Vector.column(0.8, 0.799), new Matrix(0.04)
            );

            Calculations calc = new Calculations(
                Vector.column(1.1, 0.8), kf.initialProcessCovariance(
                    // Higher initial process covariance = higher amplitude
                    Vector.column(840.972, 400.52)
                )
            );

            for (int i = 0; i < sampleBuffer.length - 1; i += 2) {

                Vector measurement = Vector.column(
                    sampleBuffer[i], sampleBuffer[i + 1]
                );

                Calculations updatedCalc = kf.execute(calc, measurement);

                sampleBuffer[i] = (float) updatedCalc.stateEstimate().get(0);
                sampleBuffer[i + 1] = (float) updatedCalc.stateEstimate().get(
                    1
                );
                calc = new Calculations(
                    updatedCalc.stateEstimate(), updatedCalc.processCovariance()
                );
            }

            System.out.println(
                "Noise removal complete for sample " + sourceAudio.getName()
                    + "! Writing to output file now..."
            );

            byte[] binaryBuffer = SoundManager.fromBufferToAudioBytes(
                sampleBuffer
            );

            sm.writeToOutputFile(
                binaryBuffer, sampleBuffer.length, outputAudio
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void useSpectralSubtraction() {
        try {
            FFTResult noiseProfile = new QuiFFT(
                "data/noiseremoval/noiseprofiles/profile5.wav"
            ).fullFFT();

            FFTResult entireAudio = new QuiFFT(sourceAudio).fullFFT();

            ArrayList<Float> overallSampleBuffer = new ArrayList<>();

            for (int i = 0; i < entireAudio.fftFrames.length; i++) {
                FFTFrame currFrame = entireAudio.fftFrames[i];

                double waveLen = (currFrame.frameEndMs - currFrame.frameStartMs)
                    / 1000;
                int firstSampleIndex = (int) (currFrame.frameStartMs * sm
                    .getFormat().getSampleRate() / 1000);

                Float[] currFrameSamples = new Float[(int) (waveLen * sm
                    .getFormat().getSampleRate())];
                for (int j = 0; j < currFrameSamples.length; j++) {
                    currFrameSamples[j] = 0f;
                }

                for (int j = 0; j < entireAudio.fftFrames[i].bins.length; j++) {
                    FrequencyBin bin = entireAudio.fftFrames[i].bins[j];
                    bin.amplitude -= noiseProfile.fftFrames[i].bins[j].amplitude;

                    SimpleWave w = new SimpleWave(
                        bin.frequency, bin.amplitude, waveLen
                    );

                    Float[] samples = w.samples(sm.getFormat().getSampleRate());

                    for (int k = 0; k < samples.length; k++) {
                        currFrameSamples[k] += samples[k];
                    }
                }

                overallSampleBuffer.addAll(
                    firstSampleIndex, Arrays.asList(currFrameSamples)
                );
            }

            byte[] noiseRemovedByteBuffer = sm.fromBufferToAudioBytes(
                overallSampleBuffer
            );

            sm.writeToOutputFile(
                noiseRemovedByteBuffer, overallSampleBuffer.size(), new File(
                    "data/noiseremoval/output/" + sourceAudio.getName()
                )
            );
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    protected static void removeAmbientNoiseFromFiles() {

        for (int i = 5; i <= 5; i++) {
            String sourcePath = "data/noiseremoval/samples/sample".concat(
                i + ".wav"
            );
            String destPath = "data/noiseremoval/output/output".concat(
                i + ".wav"
            );

            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        new AmbientNoiseRemover(
                            new File(sourcePath), new File(destPath)
                        );
                    } catch (UnsupportedAudioFileException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }, "Sample-" + i);

            t.start();
        }
    }

    void playAudio(File f) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(f));

            clip.start();
            while (!clip.isRunning())
                Thread.sleep(10);
            while (clip.isRunning())
                Thread.sleep(10);

            clip.close();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.varunsingh.kalmanfilter.Calculations;
import com.varunsingh.kalmanfilter.KalmanFilter;
import com.varunsingh.linearalgebra.Vector;

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
        useKalmanFilter();
    }

    public static void main(String[] args) {
        removeAmbientNoiseFromFiles();
    }

    void useKalmanFilter() {
        try {
            float[] sampleBuffer = sm.loadSamplesFromWav();
            System.out.println(
                "Removing noise from audio sample " + sourceAudio.getName()
                    + " of length " + sampleBuffer.length + "..."
            );

            // Too high (> 1) measurement noise = Extreemely fluctuating,
            // nearly inaudible
            // Too low (< 0.5) square block
            // 0.6 < optimal values < 1.0
            KalmanFilter kf = new KalmanFilter(Vector.column(0.8, 0.999));

            Calculations calc = new Calculations(
                Vector.column(1.1, 0.8), kf.initialProcessCovariance(
                    // Higher initial process covariance = higher amplitude
                    Vector.column(140.972, 20.52)
                )
            );

            for (int i = 0; i < sampleBuffer.length; i++) {
                Vector measurement = Vector.column(
                    sampleBuffer[i], sampleBuffer[i]
                );
                Calculations updatedCalc = kf.execute(calc, measurement);
                sampleBuffer[i] = (float) updatedCalc.stateEstimate().get(0)
                    + (float) updatedCalc.stateEstimate().get(1);
                calc = new Calculations(
                    updatedCalc.stateEstimate(), updatedCalc.processCovariance()
                );
            }

            System.out.println(
                "Noise removal complete for sample " + sourceAudio.getName()
                    + "! Writing to output file now..."
            );

            byte[] binaryBuffer = sm.fromBufferToAudioBytes(sampleBuffer);

            sm.writeToOutputFile(
                binaryBuffer, sampleBuffer.length, outputAudio
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void removeAmbientNoiseFromFiles() {

        for (int i = 2; i <= 2; i++) {
            String sourcePath = "data/noiseremoval/fabricatedsamples/sample"
                .concat(i + ".wav");
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

package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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

    public AmbientNoiseRemover(File src, File dest)
        throws UnsupportedAudioFileException, IOException {
        sm = new SoundManager(AudioSystem.getAudioInputStream(src));

        float[] sampleBuffer = sm.loadSamplesFromWav();
        byte[] binaryBuffer = sm.fromBufferToAudioBytes(sampleBuffer);

        sm.writeToOutputFile(binaryBuffer, sampleBuffer.length, dest);
        playAudio(dest);
    }

    public static void main(String[] args) {
        removeAmbientNoiseFromFiles();
    }

    protected static void removeAmbientNoiseFromFiles() {
        try {
            for (int i = 1; i <= 7; i++) {
                String fileName = "sample" + 1 + ".wav";
                String sourcePath = "data/noiseremoval/samples/".concat(
                    fileName
                );
                String destPath = "data/noiseremoval/output/".concat(fileName);

                new AmbientNoiseRemover(
                    new File(sourcePath), new File(destPath)
                );
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
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

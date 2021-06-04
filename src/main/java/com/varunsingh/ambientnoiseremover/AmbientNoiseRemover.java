package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.varunsingh.kalmanfilter.MultiDimensionalKalmanFilter;
import com.varunsingh.kalmanfilter.SystemCycleVector;
import com.varunsingh.linearalgebra.Matrix;
import com.varunsingh.linearalgebra.Vector;
import com.varunsingh.soundmanipulation.AudioByteSet;
import com.varunsingh.soundmanipulation.AudioFileManager;
import com.varunsingh.soundmanipulation.AudioSampleSet;

/* 
    Ambient Noise Remover: A program that removes background noise from an audio file
    using the multidimensional Kalman Filter
    
    Copyright (C) 2021 Varun Singh

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
/**
 * Samples: Talk by Neil Cummings at Kunsthal Aarhus, on 11 December, as part of
 * The Perfect Institution series. Source: FreeSound, user kunsthalaarhus
 * {@link https://freesound.org/people/kunsthalaarhus/sounds/211081/}
 * 
 * @author @Borumer Varun Singh
 * @apiNote Has a constructor and a main method for testing multiple samples at
 *          once
 */
public class AmbientNoiseRemover {
    private File sourceFile;
    private AudioSampleSet waveForm;
    private AudioFormat format;

    public AmbientNoiseRemover(String src, String dest) throws UnsupportedAudioFileException, IOException {
        sourceFile = new File(src);
        waveForm = AudioSampleSet.createSampleBufferFromByteBuffer(loadSourceFileData());
    }

    public static void main(String[] args) {
        removeAmbientNoiseFromFiles();
    }

    protected static void removeAmbientNoiseFromFiles() {
        try {
            for (int i = 1; i <= 7; i++) {
                String fileName = "sample" + i + ".wav";
                String sourcePath = "data/noiseremoval/samples/".concat(fileName);
                String destPath = "data/noiseremoval/output/".concat(fileName);

                AmbientNoiseRemover anr = new AmbientNoiseRemover(sourcePath, destPath);
                anr.removeNoise(i);

            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    byte[] loadSourceFileData() throws UnsupportedAudioFileException {
        try (AudioInputStream in = AudioSystem.getAudioInputStream(sourceFile)) {
            format = in.getFormat();
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[] {};
        }
    }

    public AudioSampleSet getWaveForm() {
        return waveForm;
    }

    public void setWaveForm(AudioSampleSet waveForm) {
        this.waveForm = waveForm;
    }

    void removeNoise(int index) {
        NoiseDistinguisher noisePositionFinder = new NoiseDistinguisher(waveForm);
        List<Float> noisePositions = noisePositionFinder.findSamplesBelowNoiseThreshold();
        
        int measurementVectorSize = format.getFrameSize();

        Vector initialState = new Vector(new double[] { 0.01, 0.001, 0.04 });
        Matrix initialUncertainty = new Matrix(new double[][] { { 0.068, 0.0043, 0.03 }, { 0.02, 0.03, 0.1 }, { 0.3, 0.1, 0.2 } });
        MultiDimensionalKalmanFilter filter = new MultiDimensionalKalmanFilter();

 
        int estimatedWaveLength = format.getFrameSize();

        for (int i = 0; i < noisePositions.size() / estimatedWaveLength; i++) {
            double[] frame = new double[estimatedWaveLength];

            for (int j = 0; j < estimatedWaveLength; j++) {
                frame[j] = noisePositions.get(i * estimatedWaveLength + j);
            }

            filter.measure(new Vector(frame));
        }

        Vector noise = filter.getCurrentCycleInfo().getStateEstimate();
        AudioSampleSet noiseRemovedWaveForm = waveForm.muteNoise(noise.getVectorElements());

        AudioByteSet outputByteData = AudioByteSet.createByteBufferFromSampleBuffer(waveForm.getSampleBuffer());

        AudioFileManager.writeToOutputFile(
            format, 
            outputByteData.getByteBuffer(), 
            noiseRemovedWaveForm.getBufferSize(),
            new File("data/noiseremoval/output/output" + index + ".wav")
        );

        System.out.println(String.format("File %s has been written!", String.valueOf(index)));
    }

    void playAudio() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sourceFile));

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

package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.varunsingh.kalmanfilter.MultiDimensionalKalmanFilter;
import com.varunsingh.linearalgebra.Vector;

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
    private File destinationFile;
    private CompoundWave waveForm;

    public AmbientNoiseRemover(String src, String dest) throws UnsupportedAudioFileException, IOException {
        sourceFile = new File(src);
        destinationFile = new File(dest);
        waveForm = new CompoundWave(loadSourceFileData());
    }

    public static void main(String[] args) {
        removeAmbientNoise();
    }

    protected static void removeAmbientNoise() {
        try {
            for (int i = 1; i <= 5; i++) {
                String fileName = "sample" + i + ".wav";
                String sourcePath = "data/noiseremoval/samples/".concat(fileName);
                String destPath = "data/noiseremoval/output/".concat(fileName);

                AmbientNoiseRemover anr = new AmbientNoiseRemover(sourcePath, destPath);
                anr.distinguishNoise();
                anr.writeOutputToFile();
                System.out.println(
                    Arrays.equals(
                        anr.getWaveForm().getAudioData(),
                        AudioSystem.getAudioInputStream(new File(sourcePath)).readAllBytes()
                    )
                );

            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    byte[] loadSourceFileData() throws UnsupportedAudioFileException {
        try (AudioInputStream in = AudioSystem.getAudioInputStream(sourceFile)) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[] {};
        }
    }

    public CompoundWave getWaveForm() {
        return waveForm;
    }

    public void setWaveForm(CompoundWave waveForm) {
        this.waveForm = waveForm;
    }

    void distinguishNoise() {
        try (AudioInputStream noisySoundStream = AudioSystem.getAudioInputStream(sourceFile)) {
            try (Clip clip = AudioSystem.getClip()) {
                final float NOISE_THRESHOLD = 0.115f;
                ArrayList<Integer> noisePositions = new ArrayList<Integer>();
                byte[] audioData = loadSourceFileData();
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
                        noisePositions.add(bytesRead);
                }

                Vector initialState = new Vector(new double[] { 0.01, 0.001, 127 });
                Vector initialUncertainty = new Vector(new double[] { 0.8, 0.1, 0.6 });
                MultiDimensionalKalmanFilter filter = new MultiDimensionalKalmanFilter(initialState, initialUncertainty);
                for (int i = 0; i < noisePositions.size() - 2; i += 3) {
                    filter.measure(
                        new Vector(
                            new double[] { 
                                audioData[noisePositions.get(i)],
                                audioData[noisePositions.get(i + 1)], 
                                audioData[noisePositions.get(i + 2)] 
                            }
                        )
                    );
                }

                byte noise = (byte) filter.getCurrentCycleInfo().getStateEstimate().getVectorElements()[0];
                setWaveForm(waveForm.add(waveForm.invert(noise)));
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    void writeOutputToFile() throws FileNotFoundException {
        try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            outputStream.write(waveForm.getAudioData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

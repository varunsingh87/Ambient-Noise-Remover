package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
    private WaveForm waveForm;

    public AmbientNoiseRemover(String src, String dest) throws UnsupportedAudioFileException, IOException {
        sourceFile = new File(src);
        destinationFile = new File(dest);
        waveForm = new WaveForm(loadSourceFileData());
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 5; i++) {
                String fileName = "sample" + i + ".wav";
                String sourcePath = "data/samples/".concat(fileName);
                String destPath = "data/output/".concat(fileName);

                AmbientNoiseRemover anr = new AmbientNoiseRemover(sourcePath, destPath);
                anr.writeOutputToFile();
                // anr.playAudio();
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    byte[] loadSourceFileData() throws UnsupportedAudioFileException {
        try (AudioInputStream in = getAudioInputStream()) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[] {};
        }
    }

    AudioInputStream getAudioInputStream() throws IOException, UnsupportedAudioFileException {
        return AudioSystem.getAudioInputStream(sourceFile);
    }

    public WaveForm getWaveForm() {
        return waveForm;
    }

    public void setWaveForm(WaveForm waveForm) {
        this.waveForm = waveForm;
    }

    void playAudio() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(destinationFile));

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
        try (AudioInputStream in = getAudioInputStream()) {
            AudioSystem.write(in, AudioFileFormat.Type.WAVE, new FileOutputStream(destinationFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

}

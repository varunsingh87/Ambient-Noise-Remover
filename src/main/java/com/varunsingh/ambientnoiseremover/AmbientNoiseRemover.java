package com.varunsingh.ambientnoiseremover;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.*;

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
    private AudioInputStream audio;
    private AudioFormat format;
    private Clip sound;

    public AmbientNoiseRemover(AudioInputStream f) {
        try {
            audio = AudioSystem.getAudioInputStream(f);
            format = audio.getFormat();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        sound = getSound();
    }

    public static void main(String[] args) {
        try {
            String[] samples = { "sample1.mp3", "sample2.mp3", "sample3.mp3", "sample4.mp3" };
            for (String samplePath : samples) {
                new AmbientNoiseRemover(getStreamFromFile(samplePath));
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private static AudioInputStream getStreamFromFile(String dataPath)
            throws UnsupportedAudioFileException, IOException {
        File file = new File("data/" + dataPath);
        return AudioSystem.getAudioInputStream(file);
    }

    Clip getSound() {
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("The line is not supported");
            return null;
        }

        try {
            return (Clip) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inserts in the inverse sound wave at specified position
     * 
     * @return
     */
    Clip insertDestructiveInterference(int position) {
        return null;
    }

}

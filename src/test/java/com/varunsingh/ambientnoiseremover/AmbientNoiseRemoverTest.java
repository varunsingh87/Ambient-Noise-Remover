package com.varunsingh.ambientnoiseremover;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AmbientNoiseRemoverTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertEquals(10, 5 + 5);
    }

    @Test
    public void getSound_isCorrect() {
        AudioInputStream sample1Stream;
        try {
            sample1Stream = AudioSystem.getAudioInputStream(new File("/data/sample1.mp3"));
            assertEquals(new AmbientNoiseRemover(sample1Stream).getSound().getClass(), Clip.class);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void insertDestructiveInterference_isCorrect() {

    }
}

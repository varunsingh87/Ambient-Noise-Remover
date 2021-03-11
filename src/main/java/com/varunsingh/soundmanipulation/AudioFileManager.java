package com.varunsingh.soundmanipulation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * A manager for audio files
 */
public class AudioFileManager {
    public static boolean writeToOutputFile(AudioFormat format, byte[] byteBuffer, int sampleBufferLength, File outputFile) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer)) {
            try (AudioInputStream ais = new AudioInputStream(bais, format, sampleBufferLength)) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

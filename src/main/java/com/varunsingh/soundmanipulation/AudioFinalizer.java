package com.varunsingh.soundmanipulation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class AudioFinalizer {
    private AudioFormat format;
    private byte[] byteBuffer;
    private int sampleBufferLength;

    public AudioFinalizer(AudioFormat f, byte[] buf, int len) {
        format = f;
        byteBuffer = buf;
        sampleBufferLength = len;
    }

    boolean writeToOutputFile(File outputFile) {
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

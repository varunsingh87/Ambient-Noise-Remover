package com.varunsingh.ambientnoiseremover;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * All-encompassing Sound Manager
 */
public class SoundManager {
	AudioInputStream audioInputStream;

	SoundManager(AudioInputStream ais) {
		audioInputStream = ais;
	}

	/**
	 * Loads the samples from the audio file
	 * 
	 * @implNote             Uses code by Phil Freihofner - AudioCue#loadUrl()
	 * @see                  https://github.com/philfrei/AudioCue/blob/040095986eefd76cba4435d6cdcf25c402bc99e0/src/main/java/com/adonax/audiocue/AudioCue.java#L359
	 * @return               The sample buffer
	 * @throws   IOException if the file cannot be read into a sample buffer
	 */
	float[] loadSamplesFromWav() throws IOException {
		int framesCount = 0;
		if (audioInputStream.getFrameLength() > Integer.MAX_VALUE >> 1) {
			System.out.println("WARNING: Clip is too large to entirely fit!");
			framesCount = Integer.MAX_VALUE >> 1;
		} else {
			framesCount = (int) audioInputStream.getFrameLength();
		}

		// stereo output, so two entries per frame
		float[] temp = new float[framesCount * 2];
		long tempCountdown = temp.length;

		int bytesRead = 0;
		int bufferIdx;
		int clipIdx = 0;
		byte[] buffer = new byte[1024];
		while ((bytesRead = audioInputStream.read(buffer, 0, 1024)) != -1) {
			bufferIdx = 0;
			for (int i = 0, n = (bytesRead >> 1); i < n; i++) {
				if (tempCountdown-- >= 0) {
					temp[clipIdx++] = (buffer[bufferIdx++] & 0xff)
						| (buffer[bufferIdx++] << 8);
				}
			}
		}

		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i] / 32767f;
		}

		return temp;
	}

	/**
	 * Writes to output file
	 * 
	 * @param  byteBuffer         The byte buffer containing the audio data
	 * @param  sampleBufferLength The length of the sample buffer
	 * @param  outputFile         The file to write the audio to
	 * @return                    Whether the file was written
	 */
	boolean writeToOutputFile(byte[] byteBuffer, int sampleBufferLength, File outputFile) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer)) {
			try (AudioInputStream ais = new AudioInputStream(
				bais, audioInputStream.getFormat(), sampleBufferLength
			)) {
				AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outputFile);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Converts the sample buffer to audio bytes
	 * 
	 * @implNote              Uses code by Phil Freihofner - AudioCue#loadUrl()
	 * @see                   https://github.com/philfrei/AudioCue/blob/040095986eefd76cba4435d6cdcf25c402bc99e0/src/main/java/com/adonax/audiocue/AudioCue.java#L1387
	 * @param    sampleBuffer The sample buffer
	 * @return                The audio bytes
	 */
	byte[] fromBufferToAudioBytes(float[] buffer) {
		byte[] audioBytes = new byte[buffer.length * 2];

		for (int i = 0, n = buffer.length; i < n; i++) {
			buffer[i] *= 32767;

			audioBytes[i * 2] = (byte) buffer[i];
			audioBytes[i * 2 + 1] = (byte) ((int) buffer[i] >> 8);
		}

		return audioBytes;
	}
}

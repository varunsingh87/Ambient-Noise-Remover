package com.varunsingh.ambientnoiseremover;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * All-encompassing Sound Manager
 */
public class SoundManager {
	private AudioInputStream audioInputStream;
	private AudioFormat format;

	SoundManager(AudioInputStream ais) {
		audioInputStream = ais;
		setFormat(ais.getFormat());
	}

	public static void main(String[] args) {
		SimpleWave w = new SimpleWave(400, 0.2, 20);
		SimpleWave w2 = new SimpleWave(800, 1, 20);
		Float[] w1Samples = w.samples(44100);
		// Float[] w2Samples = w2.samples(44100);

		// float[] samples = new float[w1Samples.length];
		// for (int i = 0; i < w1Samples.length; i++) {
		// samples[i] = w1Samples[i] + w2Samples[i];
		// }
		byte[] bb = fromBufferToAudioBytes(w1Samples);
		AudioFormat audioF = new AudioFormat(44100, 16, 1, true, true);
		SoundManager.writeToOutputFile(
			bb, audioF, w1Samples.length, new File(
				"data/noiseremoval/output/output1.wav"
			)
		);

	}

	/**
	 * @return the format
	 */
	public AudioFormat getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	/**
	 * Loads the samples from the audio file
	 * 
	 * @implNote             Uses code by Phil Freihofner - AudioCue#loadUrl()
	 * @see                  https://github.com/philfrei/AudioCue/blob/040095986eefd76cba4435d6cdcf25c402bc99e0/src/main/java/com/adonax/audiocue/AudioCue.java#L359
	 * @return               The sample buffer
	 * @throws   IOException if the file cannot be read into a sample buffer
	 */
	Float[] loadSamplesFromWav() throws IOException {
		int framesCount = 0;
		if (audioInputStream.getFrameLength() > Integer.MAX_VALUE >> 1) {
			System.out.println("WARNING: Clip is too large to entirely fit!");
			framesCount = Integer.MAX_VALUE >> 1;
		} else {
			framesCount = (int) audioInputStream.getFrameLength();
		}

		// stereo output, so two entries per frame
		Float[] temp = new Float[framesCount * 2];
		long tempCountdown = temp.length;

		int bytesRead = 0;
		int bufferIdx;
		int clipIdx = 0;
		byte[] buffer = new byte[1024];
		while ((bytesRead = audioInputStream.read(buffer, 0, 1024)) != -1) {
			bufferIdx = 0;
			for (int i = 0, n = (bytesRead >> 1); i < n; i++) {
				if (tempCountdown-- >= 0) {
					temp[clipIdx++] = (float) ((buffer[bufferIdx++] & 0xff)
						| (buffer[bufferIdx++] << 8));
				}
			}
		}

		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i] / 32767f;
		}

		return temp;
	}

	static boolean writeToOutputFile(byte[] bb, AudioFormat format, int sampleBufferLength, File outputFile) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(bb)) {
			try (AudioInputStream ais = new AudioInputStream(
				bais, format, sampleBufferLength
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
	static byte[] fromBufferToAudioBytes(Float[] buffer) {
		byte[] audioBytes = new byte[buffer.length * 2];

		for (int i = 0, n = buffer.length; i < n; i++) {
			buffer[i] *= 32767;

			audioBytes[i * 2] = (byte) buffer[i].byteValue();
			audioBytes[i * 2 + 1] = (byte) (buffer[i].intValue() >> 8);
		}

		return audioBytes;
	}

	byte[] fromBufferToAudioBytes(ArrayList<Float> buffer) {
		byte[] audioBytes = new byte[buffer.size() * 2];

		for (int i = 0, n = buffer.size(); i < n; i++) {
			buffer.set(i, buffer.get(i) * 32767f);

			audioBytes[i * 2] = buffer.get(i).byteValue();
			audioBytes[i * 2 + 1] = (byte) ((int) buffer.get(i)
				.floatValue() >> 8);
		}

		return audioBytes;
	}
}

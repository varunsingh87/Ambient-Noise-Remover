package com.varunsingh.ambientnoiseremover;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class NoiseDistinguisherTest {
    @Test
    public void testFindBytesWithAmplitudesBelowNoiseThreshold() {
        NoiseDistinguisher noiseDistinguisher = new NoiseDistinguisher(new File("data/noiseremoval/samples/sample2.wav"));

        List<Float> samplesWithAmplitudesBelowNoiseThreshold = noiseDistinguisher.findSamplesBelowNoiseThreshold();

        samplesWithAmplitudesBelowNoiseThreshold.forEach(s -> {
            assertTrue(s < NoiseDistinguisher.NOISE_THRESHOLD);
        });
    } 
}

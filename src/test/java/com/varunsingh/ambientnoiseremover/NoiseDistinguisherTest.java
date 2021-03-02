package com.varunsingh.ambientnoiseremover;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class NoiseDistinguisherTest {
    @Test
    public void testFindBytesWithAmplitudesBelowNoiseThreshold() {
        NoiseDistinguisher noiseDistinguisher = new NoiseDistinguisher(new File("data/noiseremoval/samples/sample2.wav"));

        HashMap<Integer, Float> bytesWithAmplitudesBelowNoiseThreshold = noiseDistinguisher.findSamplesBelowNoiseThreshold();
        Iterator<Map.Entry<Integer, Float>> entrySet = bytesWithAmplitudesBelowNoiseThreshold.entrySet().iterator();

        while (entrySet.hasNext()) {
            float entryValue = entrySet.next().getValue();
            assertTrue(entryValue < NoiseDistinguisher.NOISE_THRESHOLD);
        }
    } 
}

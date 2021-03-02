package com.varunsingh.soundmanipulation;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Ignore;
import org.junit.Test;

public class CompoundWaveTest {
    @Test
    @Ignore
    public void testAdd() {
        byte[] firstByteArr = { 5, 8, -12, 9, 0, 3 };
        byte[] secondByteArr = { 2, 3, 9, 0, 1, 1 };

        CompoundWave waveForm = new CompoundWave(firstByteArr).add(new CompoundWave(secondByteArr));

        assertArrayEquals("The byte data is different", waveForm.getAudioData(), new byte[] { 7, 11, -3, 9, 1, 4 });
    }

    @Test
    @Ignore
    public void testMute() {
        byte[] origByteArr = {-2, -9, 45, 13, 1, 10, 10, -1, 0, 0, 99, 120};
        CompoundWave waveForm = new CompoundWave(origByteArr);

        assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, waveForm.mute().getAudioData());
    }

    @Test
    @Ignore
    public void testInvert() {
        byte[] firstByteArr = { 1, 0, -86, 124 };

        byte[] actualInversion = new CompoundWave(firstByteArr).invert().getAudioData();

        assertArrayEquals(new byte[] { -1, 0, 86, -124 }, actualInversion);
    }
}

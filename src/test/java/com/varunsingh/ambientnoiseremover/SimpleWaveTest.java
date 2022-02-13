package com.varunsingh.ambientnoiseremover;

import static org.junit.Assert.assertFalse;

import com.varunsingh.ambientnoiseremover.SimpleWave;

import org.junit.Test;

public class SimpleWaveTest {
    @Test
    public void test_givenWaveWithoutSeconds_whenInstantiated_thenHasFiniteTimeValueIsFalse() {
        SimpleWave waveWithInfiniteDomain = new SimpleWave(415, 0.5);
        boolean hasTimeValue = waveWithInfiniteDomain.hasFiniteTimeValue();
        assertFalse(
            "SimpleWave#hasFiniteTimeValue() should return false when seconds is not given",
            hasTimeValue
        );
    }
}

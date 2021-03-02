package com.varunsingh.soundmanipulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleWaveTest {
    @Test
    public void test_givenWaveWithoutSeconds_whenInstantiated_thenHasFiniteTimeValueIsFalse() {
        SimpleWave waveWithInfiniteDomain = new SimpleWave(415, 0.5);
        boolean hasTimeValue = waveWithInfiniteDomain.hasFiniteTimeValue();
        assertFalse("SimpleWave#hasFiniteTimeValue() should return false when seconds is not given", hasTimeValue);
    }

    @Test
    public void test_givenTwoSimpleWaves_whenAppend_thenTimeIsSumOfTimeOfTwoWaves() {
        SimpleWave wave1 = new SimpleWave(390, 0.5, 4);
        SimpleWave wave2 = new SimpleWave(400, 0.6, 5);
        
        CompoundWave concatenatedForm = wave1.append(wave2);

        assertEquals(9, concatenatedForm.getSeconds(), 0.1);
    }
}

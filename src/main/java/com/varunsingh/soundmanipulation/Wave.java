package com.varunsingh.soundmanipulation;

public interface Wave {
    Wave add(Wave addendWave);

    Wave append(Wave waveToConcat);

    Wave invert();
}

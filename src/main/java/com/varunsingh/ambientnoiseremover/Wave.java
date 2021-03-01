package com.varunsingh.ambientnoiseremover;

public interface Wave {
    Wave add(Wave addendWave);

    Wave invert();
}

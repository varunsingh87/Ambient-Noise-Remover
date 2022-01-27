package com.varunsingh.kalmanfilter;

import com.varunsingh.linearalgebra.Vector;

public record Calculations(double kalmanGain, Vector estimate, Vector processError) {
    Calculations(Vector e) {
        this(0.0, e, Vector.column(0.0, 0.0));
    }
}

package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.varunsingh.linearalgebra.Vector.VectorType;

import org.junit.Test;

public class VectorTest {
    @Test
    public void testTranspose() {
        assertEquals(
            new Vector(
                new double[] { 5, 7, 6 }
            ), 
            new Vector(
                new double[] { 5, 7, 6 }, 
                VectorType.COLUMN
            )
        );
    }

    @Test
    public void testCalcLength() {
        Vector sampleVector = new Vector(new double[] { 2, 5 });

        double calculatedLength = sampleVector.calcLength();

        assertEquals(Math.sqrt(29), calculatedLength, 0.0);
    }


}

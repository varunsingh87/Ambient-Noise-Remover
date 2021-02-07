package com.varunsingh.linearalgebra;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VectorTest {
    @Test
    public void testVectorInstantiation() {
        Vector v = new Vector(0, -1, 2);
        
        assertEquals(0, v.getX(), 0);
        assertEquals(-1, v.getY(), 0);
        assertEquals(2, v.getZ(), 0);
        assertEquals(0, v.getxVelocity(), 0);
        assertEquals(0, v.getyVelocity(), 0);
        assertEquals(0, v.getzVelocity(), 0);
    }

    @Test
    public void testPositionAndVelocityVectorInstantiation() {
        Vector vectorWithVelocity = new Vector(30, 50, 60, -1, 5, 2);

        assertEquals(30, vectorWithVelocity.getX(), 0);
        assertEquals(50, vectorWithVelocity.getY(), 0);
        assertEquals(60, vectorWithVelocity.getZ(), 0);
        assertEquals(-1, vectorWithVelocity.getxVelocity(), 0);
        assertEquals(5, vectorWithVelocity.getyVelocity(), 0);
        assertEquals(2, vectorWithVelocity.getzVelocity(), 0);
    }
}

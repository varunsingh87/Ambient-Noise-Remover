package com.varunsingh.linearalgebra;

public class VectorTriangle {
    private Vector sideA;
    private Vector sideB;
    private Vector sideC;
    private double sideCLength;

    public VectorTriangle(Vector a, Vector b) {
        sideA = a;
        sideB = b;
        sideCLength = sideA.minus(sideB).calcLength();
    }

    public VectorTriangle(Vector a, Vector b, Vector c) {
        sideA = a;
        sideB = b;
        sideC = c;
        sideCLength = c.calcLength();
    }

    public double getAngle(short angleNumber) {
        switch (angleNumber) {
            case 1:
                return solveLawOfCosines(sideA.calcLength(), sideB.calcLength(), sideCLength);
            case 2:
                return solveLawOfCosines(sideB.calcLength(), sideCLength, sideA.calcLength());
            case 3:
                return solveLawOfCosines(sideCLength, sideA.calcLength(), sideB.calcLength());
            default:
                throw new IllegalArgumentException("Parameter angleNumber must be either 1, 2, or 3");
        }
    }

    /**
     * Plugs in the lengths of two sides and the angle therebetween into the Law of
     * Cosines formula
     * 
     * @param side1        The length of a side that touches angle C
     * @param side2        The length of the other side that touches angle C
     * @param angleBetween The angle between the two sides
     * @return the length of the opposite side
     */
    public static double useLawOfCosines(double side1, double side2, double angleBetween) {
        return Math.sqrt(Math.pow(side1, 2) + Math.pow(side2, 2) + 2 * side1 * side2 * Math.cos(angleBetween));
    }

    /**
     * Solves for C in Law of Cosines formula
     * 
     * @param side1 The length of a side that touches angle C
     * @param side2 The length of the other side that touches angle C
     * @param side3 The length of the side opposite to angle C
     * @return the angle measure of C in degrees
     */
    public static double solveLawOfCosines(double side1, double side2, double side3) {
        return Math.round(
            Math.toDegrees(
                Math.acos(
                    (
                        Math.pow(side3, 2) - 
                        Math.pow(side1, 2) - 
                        Math.pow(side2, 2)
                    ) 
                    /
                    -(2 * side1 * side2)
                )
            ) 
            * 100
        ) 
        / 100;
    }
}

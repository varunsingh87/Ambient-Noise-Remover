package com.varunsingh.linearalgebra;

public class Vector {
    private Matrix vectorElements;
    
    public Vector(double x, double y, double z) {
        vectorElements = new Matrix();
        vectorElements.setMatrixElements(new double[][] { {x, y, z } });
    }

    public Vector(double x, double y, double z, double xVel, double yVel, double zVel) {
        vectorElements = new Matrix();
        vectorElements.setMatrixElements(new double[][] { {x, y, z, xVel, yVel, zVel } });
    }

    public Matrix getMatrix() {
        return vectorElements;
    }

    public double[] getVectorElements() {
        return getMatrix().getMatrixElements()[0];
    }

    public void setVectorElement(int index, double newValue) {
        double[][] temp = vectorElements.getMatrixElements();
        temp[0][index] = newValue;
        vectorElements.setMatrixElements(temp);
    }

    public double getX() {
        return (double) getVectorElements()[0];
    }

    public void setX(double xPosition) {
        setVectorElement(0, xPosition);
    }

    public double getY() {
        return (double) getVectorElements()[1];
    }

    public void setY(double yPosition) {
        setVectorElement(1, yPosition);
    }

    public double getZ() {
        return getVectorElements().length >= 3 ? (double) getVectorElements()[2] : 0;
    }

    public void setZ(double zPosition) {
        setVectorElement(2, zPosition);
    }

    public double getxVelocity() {
        return getVectorElements().length >= 4 ? (double) getVectorElements()[3] : 0;
    }

    public void setxVelocity(double xVelocity) {
        setVectorElement(3, xVelocity);
    }

    public double getyVelocity() {
        return getVectorElements().length >= 5 ? (double) getVectorElements()[4] : 0;
    }

    public void setyVelocity(double yVelocity) {
        setVectorElement(4, yVelocity);
    }

    public double getzVelocity() {
        return getVectorElements().length >= 6 ? (double) getVectorElements()[5] : 0;
    }

    public void setzVelocity(double zVelocity) {
        setVectorElement(5, zVelocity);
    }
    
    public int getRows() {
        return getVectorElements().length;
    }

    public int getColumns() {
        return 1;
    }
}

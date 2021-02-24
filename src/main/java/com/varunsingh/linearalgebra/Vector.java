package com.varunsingh.linearalgebra;

public class Vector extends Matrix {
    private double[] vectorElements;

    public Vector(double[] v) {
        super(loadVectorElements(v));
        vectorElements = v;
    }

    private static double[][] loadVectorElements(double[] v) {
        double[][] toConstruct = new double[v.length][1];
        for (int i = 0; i < toConstruct.length; i++) {
            toConstruct[i][0] = v[i];
        }
        return toConstruct;
    }

    public double[] getVectorElements() {
        return vectorElements;
    }

    public void setVectorElements(double[] newVector) {
        vectorElements = newVector;
    }

    public double get(int index) {
        return getMatrixElements()[index][0];
    }

    public void setVectorElement(int rowIndex, double newValue) {
        double[][] temp = getMatrixElements();
        temp[rowIndex][0] = newValue;
        setMatrixElements(temp);
    }

    /**
     * Calculates the standard norm of the vector in the real number space
     * @return The norm of this vector
     */
    public double calcNorm() {
        double result = 0;
        
        for (int i = 0; i < getRows(); i++) {
            result += Math.sqrt(this.get(i));
        }

        return result;
    }
}

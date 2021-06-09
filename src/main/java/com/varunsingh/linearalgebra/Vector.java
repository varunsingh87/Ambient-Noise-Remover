package com.varunsingh.linearalgebra;

public class Vector extends Matrix {
    private double[] vectorElements;

    public enum VectorType {
        ROW, COLUMN
    }

    private VectorType vectorType;

    public Vector(double[] v) {
        super(loadVectorElements(v));
        vectorElements = v;
        vectorType = VectorType.COLUMN;
    }

    public Vector(double[] v, VectorType t) {
        super(t == VectorType.ROW ? new double[][] { v } : loadVectorElements(v));
        vectorType = t;
    }

    private static double[][] loadVectorElements(double[] v) {
        double[][] toConstruct = new double[v.length][1];
        for (int i = 0; i < toConstruct.length; i++) {
            toConstruct[i][0] = v[i];
        }
        return toConstruct;
    }

    public Vector scale(double scalar) {
        return super.scale(scalar).asColumnVector();
    }

    public Vector transpose() {
        switch (vectorType) {
            case COLUMN:
                return super.transpose().asRowVector();
            case ROW:
            default:
                return super.transpose().asColumnVector();
        }
    }

    /**
     * Converts column vector in matrix form to a column vector
     */
    public static Vector valueOf(Matrix m) {
        Vector columnVector = new Vector(new double[m.getRows()]);

        for (int i = 0; i < m.getRows(); i++) {
            columnVector.setVectorElement(i, m.get(i, 0));
        }

        return columnVector;
    }

    public double[] getVectorElements() {
        return vectorElements;
    }

    public void setVectorElements(double[] newVector) {
        vectorElements = newVector;
    }

    public double get(int index) {
        switch (vectorType) {
            case ROW:
                return getMatrixElements()[0][index];
            case COLUMN:
            default:
                return getMatrixElements()[index][0];
        }
    }

    public void setVectorElement(int rowIndex, double newValue) {
        double[][] temp = getMatrixElements();
        temp[rowIndex][0] = newValue;
        setMatrixElements(temp);
    }

    public double calcInnerProduct() {
        return dot();
    }

    /**
     * Calculates the dot product of two real Kx1 vectors defined at
     * https://statlect.com/matrix-algebra/inner-product
     * 
     * @param y The vector to multiply by
     * @return The inner product
     */
    public double calcInnerProduct(Vector y) {
        return dot(y);
    }

    /**
     * Multiplies every element in each vector by every element in the other vector
     * 
     * @param y The vector to multiply this vector by
     * @return A matrix of the outer product
     */
    public Matrix calcOuterProduct(Vector y) {
        return this.times(y.transpose());
    }

    /**
     * Gets the size of the vector
     * 
     * @return the numbers of rows if this is a column vector and the number of
     *         columns if this is a row vector
     */
    int getSize() {
        return vectorType == VectorType.ROW ? getColumns() : getRows();
    }

    public double calcExpectedValue() {
        double sum = 0;

        for (int i = 0; i < getSize(); i++) {
            sum += get(i);
        }

        return sum / getSize();
    }

    public double calcCovarianceIn2x2Matrix(Vector v2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates the length of a vector
     * 
     * @return ||this|| the square root of the sum of the squares of each component
     */
    public double calcLength() {
        return Math.sqrt(dot());
    }

    /**
     * Calculates the norm of a vector
     * 
     * @return ||this|| the square root of the sum of the squares of each component
     */
    public double calcNorm() {
        return Math.sqrt(dot());
    }

    /**
     * Calculates the magnitude of a vector
     * 
     * @return ||this|| the square root of the sum of the squares of each component
     */
    public double calcMagnitude() {
        return Math.sqrt(dot());
    }

    public double dot() {
        double sumOfComponents = 0;

        for (int i = 0; i < getSize(); i++) {
            double componentSquared = get(i) * get(i);
            sumOfComponents += componentSquared;
        }

        return sumOfComponents;
    }

    public double dot(Vector v) {
        if (getSize() != v.getSize())
            throw new IllegalArgumentException("The vectors must be the same size");

        double product = 0;

        for (int i = 0; i < v.getSize(); i++) {
            product += v.get(i) * get(i);
        }

        return product;
    }

    /**
     * Evaluates the cross product of this and another vector
     * 
     * @param y The second factor
     * @return The vector cross product
     */
    public Vector cross(Vector y) {
        if (getSize() != 3 || y.getSize() != 3)
            throw new IllegalArgumentException("Vector must have 3 elements to be crossable");

        Vector crossProduct = new Vector(new double[3]);

        crossProduct.setVectorElement(0, get(1) * y.get(2) - get(2) * y.get(1));
        crossProduct.setVectorElement(1, get(2) * y.get(0) - get(0) * y.get(2));
        crossProduct.setVectorElement(2, get(0) * y.get(1) - get(1) * y.get(0));

        return crossProduct;
    }
}

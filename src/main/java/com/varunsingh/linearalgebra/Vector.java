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

    public Vector transpose() {
        switch (vectorType) {
            case COLUMN: return super.transpose().asRowVector();
            case ROW:
            default: return super.transpose().asColumnVector();
        }
    }

    /**
     * Converts matrix to a column vector
     */
    public static Vector valueOf(Matrix m) {
        Vector columnVector = new Vector(new double[m.getRows()]);

        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getColumns(); j++) {
                columnVector.set(i, 0, m.get(i, j));
            }
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

    public Vector calcInnerProduct() {
        return this.transpose().times(this).asRowVector();
    }

    /**
     * Calculates the dot product of two real Kx1 vectors
     * defined at https://statlect.com/matrix-algebra/inner-product
     * @param y The vector to multiply by
     * @return The inner product
     */
    public Vector calcInnerProduct(Vector y) {
        return y.transpose().times(this).asRowVector();
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

    /**
     * Calculates the standard norm of the vector in the real number space
     * 
     * @return The norm of this vector
     */
    public double calcNorm() {
        double result = 0;

        for (double el : vectorElements) {
            result += Math.sqrt(el);
        }

        return result;
    }

    public double calcCovarianceIn2x2Matrix(Vector v2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates the length of a vector
     * @return ||this|| the square root of the sum of the squares of each component
     */
    public double calcLength() {
        double sumOfComponents = 0;

        for (int i = 0; i < getSize(); i++) {
            double componentSquared = get(i) * get(i);
            sumOfComponents += componentSquared;
        }

        return Math.sqrt(sumOfComponents);
    }
}

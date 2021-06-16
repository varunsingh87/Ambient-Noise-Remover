package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class Vector implements Dataset {
    private double[] vectorElements;
    private double average;

    public enum VectorType {
        ROW, COLUMN
    }

    private VectorType vectorType;

    public Vector(double[] v) {
        vectorElements = v;
        vectorType = VectorType.COLUMN;
        average = calcAverage();
    }

    public Vector(double[] v, VectorType t) {
        vectorElements = v;
        vectorType = t;
        average = calcAverage();
    }

    @Override
    public boolean equals(Object o) {
        return Arrays.equals(getValues(), ((Vector) o).getValues());
    }

    public double getAverage() {
        return average;
    }

    @Override
    public Vector transpose() {
        switch (vectorType) {
            case COLUMN:
                return new Vector(vectorElements, VectorType.COLUMN);
            case ROW:
            default:
                return new Vector(vectorElements, VectorType.ROW);
        }
    }

    /**
     * Converts column vector in matrix form to a column vector
     */
    public static Vector valueOf(Matrix m) {
        Vector columnVector = new Vector(new double[m.getRows()]);

        for (int i = 0; i < m.getRows(); i++) {
            columnVector.set(i, m.get(i, 0));
        }

        return columnVector;
    }

    /**
     * Gets the elements in the dataset
     * 
     * @return The elements in a double array
     */
    public double[] getValues() {
        return vectorElements;
    }

    public void setValues(double[] newVector) {
        vectorElements = newVector;
    }

    public double get(int index) {
        return vectorElements[index];
    }

    public void set(int rowIndex, double newValue) {
        vectorElements[rowIndex] = newValue;
    }

    @Override
    public Dataset times(Dataset factor) {
        if (getColumns() != factor.getRows())
            throw new IllegalArgumentException();

        switch (vectorType) {
            case ROW: {
                Vector product = new Vector(new double[getColumns()], VectorType.ROW);
                Matrix mFactor = factor instanceof Matrix ? (Matrix) factor : new Matrix(new double[][] { ((Vector) factor).getValues() });
                
                for (int j = 0; j < factor.getColumns(); j++) {
                    double sum = 0;

                    for (int k = 0; k < getColumns(); k++) {

                        double firstFactor = vectorElements[k];
                        double secondFactor = mFactor.get(k, j);

                        sum += firstFactor * secondFactor;
                    }

                    product.set(j, sum);
                }

                return product;
            }
            case COLUMN:
            default: {
                Matrix product = new Matrix(new double[getRows()][factor.getColumns()]);
                Vector vFactor = factor instanceof Vector ? (Vector) factor : ((Matrix) factor).getRow(0);

                for (int i = 0; i < getRows(); i++) {
                    for (int j = 0; j < factor.getColumns(); j++) {
                        product.set(i, j, get(i) * vFactor.get(j));
                    }
                }

                return product;
            }
        }
    }

    @Override
    public Vector scale(double scalar) {
        return new Vector(Arrays.stream(vectorElements).map(v -> v * scalar).toArray());
    }

    @Override
    public Vector plus(Dataset addend) {
        Vector sum = new Vector(new double[getSize()]);

        for (int i = 0; i < vectorElements.length; i++) {
            sum.set(i, get(i) + ((Vector) addend).get(i));
        }

        return sum;
    }

    @Override
    public Vector minus(Dataset subtrahend) {
        return plus(subtrahend.scale(-1));
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
    public Dataset calcOuterProduct(Vector y) {
        return this.times(y.transpose());
    }

    int getSize() {
        return vectorElements.length;
    }

    /**
     * Gets the size of the vector
     * 
     * @return 1 if a row vector, otherwise length of {@link #vectorElements}
     */
    @Override
    public int getRows() {
        return vectorType == VectorType.ROW ? 1 : vectorElements.length;
    }

    /**
     * Gets the number of columns in the vector
     * 
     * @return 1 if a column vector, otherwise length of {@link #vectorElements}
     */
    @Override
    public int getColumns() {
        return vectorType == VectorType.COLUMN ? 1 : vectorElements.length;
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

        Vector crossProduct = new Vector(new double[] {
            get(1) * y.get(2) - get(2) * y.get(1),
            get(2) * y.get(0) - get(0) * y.get(2),
            get(0) * y.get(1) - get(1) * y.get(0)
        });
        
        return crossProduct;
    }

    /**
     * Calculates E(this)
     * 
     * @see {@link #calcAverage()}
     */
    public double calcExpectedValue() {
        return calcAverage();
    }

    /**
     * Calculates the summation(i=1, xi)/N
     * 
     * @return The average of the elements of this dataset
     */
    private double calcAverage() {
        double sumOfValues = 0;

        for (int i = 0; i < getSize(); i++) {
            sumOfValues += get(i);
        }

        return sumOfValues /= getSize();
    }

    /**
     * Around 100% of values are between positive and negative sigma squared
     * 
     * @return The positive variance (Sigma^2)
     */
    public double calcVariance() {
        double average = calcAverage();
        double deviationSum = 0;

        for (int i = 0; i < getSize(); i++) {
            double deviation = Math.abs(average - get(i));
            deviationSum += deviation * deviation;
        }

        return MatrixRound.roundDouble(deviationSum / getSize(), 5);
    }

    /**
     * Around 68.3% of all values are between positive and negative sigma
     * 
     * @return The positive standard deviation (Sigma)
     */
    public double calcStandardDeviation() {
        return Math.sqrt(calcVariance());
    }
}

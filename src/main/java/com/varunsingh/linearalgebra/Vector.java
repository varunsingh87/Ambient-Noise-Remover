package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class Vector implements Dataset {
    private double[] vectorElements;
    private double average;

    public enum VectorType {
        ROW, COLUMN
    }

    private VectorType vectorType;

    public Vector(int size, VectorType t) {
        vectorElements = new double[size];
        vectorType = t;
    }

    public Vector(double... v) {
        vectorElements = v;
        vectorType = VectorType.COLUMN;
        average = calcAverage();
    }

    public Vector(double[] v, VectorType t) {
        vectorElements = v;
        vectorType = t;
        average = calcAverage();
    }

    public VectorType getOrientation() {
        return vectorType;
    }

    @Override
    public boolean equals(Object o) {
        return Arrays.equals(getValues(), ((Vector) o).getValues());
    }

    @Override
    public String toString() {
        return Arrays.toString(getValues());
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
    public static Vector valueOf(Dataset m) {
        if (m.getRows() == 1 && m.getColumns() > 1 && m instanceof Matrix) {
            return Vector.row((Matrix) m);
        }

        Vector columnVector = new Vector(new double[m.getRows()]);

        for (int i = 0; i < m.getRows(); i++) {
            columnVector.set(i, m instanceof Matrix ? ((Matrix) m).get(i, 0) : ((Vector) m).get(i));
        }

        return columnVector;
    }

    public static Vector row(double... els) {
        return new Vector(els, VectorType.ROW);
    }

    public static Vector row(Matrix m) {
        return m.getRow(0);
    }

    public static Vector column(double... els) {
        return new Vector(els, VectorType.COLUMN);
    }

    public static Vector scalar(double s) {
        return new Vector(new double[] { s }, VectorType.COLUMN);
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

    public Dataset times(Dataset factor) {
        if (getColumns() != factor.getRows()) {
            throw new IllegalArgumentException("Vector column count does not match dataset row count");
        }

        // Row vector
        if (vectorType == VectorType.ROW) {
            if (factor.getColumns() == 1) { // Row vector by getRows() x 1 column vector --> 1x1 matrix
                return new Vector(dot(Vector.valueOf(factor)));
            } else { // Row vector by getColumns() x n matrix --> 1 x factor.getColumns() row vector
                return multiplyRowByMatrix((Matrix) factor);
            }
        } else { // this is a column vector
            // This=Column vector
            // Factor= 1 x n row vector
            // Product: m x n matrix
            return multiplyColumnByRow(Vector.valueOf(factor));
        }
    }

    private Matrix multiplyColumnByRow(Vector factor) {
        Matrix product = new Matrix(getRows(), factor.getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < factor.getColumns(); j++) {
                product.set(i, j, get(i) * factor.get(j));
            }
        }
        return product;
    }

    private Vector multiplyRowByMatrix(Matrix factor) {
        Vector product = new Vector(factor.getColumns(), VectorType.ROW);

        for (int i = 0; i < factor.getColumns(); i++) {
            double sum = 0;

            for (int j = 0; j < factor.getRows(); j++) {
                sum += get(j) * factor.get(j, i);
            }

            product.set(i, sum);
        }

        return product;
    }
    
    @Override
    public Vector scale(double scalar) {
        return new Vector(Arrays.stream(vectorElements).map(v -> v * scalar).toArray());
    }

    @Override
    public Dataset plus(Dataset addend) {
        if (addend instanceof Matrix && ((Matrix) addend).isConventionalMatrix()) {
            throw new IllegalArgumentException("Cannot add non-vector to vector");
        }

        Vector vAddend = Vector.valueOf(addend);

        if (getSize() != vAddend.getSize() || getOrientation() != vAddend.getOrientation()) {
            throw new IllegalArgumentException("Vectors must be of equal size and the same orientation");
        }

        Vector sum = new Vector(new double[getSize()]);

        for (int i = 0; i < vectorElements.length; i++) {
            sum.set(i, get(i) + (Vector.valueOf(addend)).get(i));
        }

        return sum;
    }

    @Override
    public Vector minus(Dataset subtrahend) {
        return (Vector) plus(subtrahend.scale(-1));
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
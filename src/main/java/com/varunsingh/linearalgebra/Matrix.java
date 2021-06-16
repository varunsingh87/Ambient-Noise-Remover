package com.varunsingh.linearalgebra;

import java.util.Arrays;

import com.varunsingh.linearalgebra.Vector.VectorType;

public class Matrix implements Dataset {
    protected double[][] matrixElements;
    protected Vector[] vectorRows;

    /**
     * An exception that is thrown when a matrix is attempted to be inverted when it
     * is not an invertible matrix
     */
    public final static class MatrixNotInvertibleException extends Exception {
        private static final long serialVersionUID = -5031211581058588672L;

        MatrixNotInvertibleException() {
            super("The matrix must be square to calculate the inverse");
        }
    }

    public Matrix() {
    }

    public Matrix(double scalar) {
        setMatrixElements(new double[][] { { scalar } });
    }

    /**
     * Constructs a Matrix using a two-dimensional array
     * @param m The two-dimensional array containing all the matrix elements
     */
    public Matrix(double[][] m) {
        vectorRows = new Vector[m.length];
        setMatrixElements(m);
    }

    /**
     * Constructs a matrix by row vectors
     * @param rows The set of vectors that are merged together to create a matrix
     * @return A, the Matrix object
     */
    public Matrix(Vector[] rows) {
        matrixElements = new double[rows.length][rows[0].getSize()];
        
        for (int i = 0; i < rows.length; i++) {
            matrixElements[i] = rows[i].getValues();
        }

        vectorRows = rows;
    }

    public double[][] getMatrixElements() {
        return matrixElements;
    }

    public void setMatrixElements(double[][] matrixElements) throws IllegalArgumentException {
        if (!validateMatrix(matrixElements)) throw new IllegalArgumentException();

        for (int i = 0; i < matrixElements.length; i++) {
            vectorRows[i] = new Vector(matrixElements[i]);
        }

        this.matrixElements = matrixElements;
    }

    public static boolean validateMatrix(double[][] elsToValidate) {
        if (elsToValidate.length < 1) return true;
        
        int columns = elsToValidate[0].length;
        for (int i = 0; i < elsToValidate.length; i++) {
            if (columns != elsToValidate[i].length)
                return false;
        }

        return true;
    }

    public double get(int row, int col) {
        return matrixElements[row][col];
    }

    public Vector getRow(int row) {
        return vectorRows[row];
    }

    public void set(int firstLvlIndex, int secondLvlIndex, double newValue) {
        matrixElements[firstLvlIndex][secondLvlIndex] = newValue;
    }

    public int getRows() {
        return matrixElements.length;
    }

    public int getColumns() {
        return matrixElements[0].length;
    }

    @Override
    public boolean equals(Object matrix) {
        return Arrays.deepEquals(getMatrixElements(), ((Matrix) matrix).getMatrixElements());
    }

    public String toString() {
        return Arrays.deepToString(matrixElements);
    }

    public Dataset times(Dataset d) {
        if (getColumns() != d.getRows())
            throw new IllegalArgumentException();

        Matrix m = (Matrix) d;

        Matrix toReturn = new Matrix(new double[getRows()][m.getColumns()]);

        for (int i = 0; i < getRows(); i++) {

            for (int j = 0; j < m.getColumns(); j++) {
                double sum = 0;

                for (int k = 0; k < getColumns(); k++) {
                    
                    double firstFactor = matrixElements[i][k];
                    double secondFactor = m.get(k, j);

                    sum += firstFactor * secondFactor;
                }

                toReturn.set(i, j, sum);
            }

        }

        return toReturn;
    }

    public Dataset times(Vector d) {
        return times(new Matrix(new Vector[] { d }));
    }

    /**
     * Adds two matrices together
     * 
     * @param addend The matrix to add to the current matrix
     * @return The sum of the two matrices
     * @throws IllegalArgumentException When the matrices cannot be added
     */
    public Matrix plus(Dataset addend) {
        if (!(getRows() == addend.getRows() && getColumns() == addend.getColumns()))
            throw new IllegalArgumentException("Cannot add matrices of different dimensions");

        Matrix mAddend = (Matrix) addend;
        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(
                    i, 
                    j, 
                    matrixElements[i][j] + mAddend.get(i, j)
                );
            }
        }

        return toReturn;
    }

    /**
     * Convenience method for adding to scalar -1 times a matrix
     * 
     * @param minuend The matrix being subtracted
     * @return The difference matrix
     */
    public Dataset minus(Dataset minuend) {
        return this.plus(minuend.scale(-1));
    }

    @Override
    public Matrix scale(double scalar) {
        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(i, j, MatrixRound.roundDouble(scalar * matrixElements[i][j], 5));
            }
        }

        return toReturn;
    }

    @Override
    public Matrix transpose() {
        Matrix toReturn = new Matrix(new double[getColumns()][getRows()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(j, i, matrixElements[i][j]);
            }
        }

        return toReturn;
    }

    /**
     * Inverts the matrix using Gauss-Jordan elimination
     * 
     * @return The inverted matrix
     * @throws MatrixNotInvertibleException
     */
    public Matrix invert() throws MatrixNotInvertibleException {
        return new MatrixInverseOperation(this).compute();
    }

    public static Matrix createIdentityMatrix(int size) {
        double[][] identityElements = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identityElements[i][j] = i == j ? 1 : 0;
            }
        }

        return new Matrix(identityElements);
    }

    public static Matrix createDiagonalMatrix(int dimens, double value) {
        double[][] toReturn = new double[dimens][dimens];

        for (int i = 0; i < dimens; i++) {
            
            for (int j = 0; j < dimens; j++) {
                if (i == j) toReturn[i][i] = value;
                else toReturn[i][j] = 0;
            }
        }

        return new Matrix(toReturn);
    }

    public double getDeterminant() throws MatrixNotInvertibleException {
        if (!isSquare())
            throw new Matrix.MatrixNotInvertibleException();

        if (getRows() == 2) {
            double firstTerm = matrixElements[0][0] * matrixElements[1][1];
            double secondTerm = matrixElements[0][1] * matrixElements[1][0];

            return 1.0 / (firstTerm - secondTerm);
        } else {
            return 0;
        }
    }
    
    public boolean isSquare() {
        return matrixElements.length == matrixElements[0].length;
    }

    public boolean isIdentityMatrix() {
        if (!isSquare())
            return false;

        for (int i = 0; i < getRows(); i++) {
            if (matrixElements[i][i] != 1)
                return false;

            for (int j = 0; j < getColumns(); j++) {
                if (j != i && matrixElements[i][j] != 0)
                    return false;
            }

        }

        return true;
    }

    public boolean isDiagonal() throws IllegalArgumentException {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (i != j && matrixElements[i][j] != 0) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public boolean isInverse(Matrix inverse) {
        return ((Matrix) this.times(inverse)).isIdentityMatrix() 
            && ((Matrix) inverse.times(this)).isIdentityMatrix();
    }

    public Vector asRowVector() {
        return new Vector(this.getMatrixElements()[0], VectorType.ROW);
    }

    public Vector asColumnVector() {
        return Vector.valueOf(this);
    }
}

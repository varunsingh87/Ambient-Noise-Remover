package com.varunsingh.linearalgebra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class Matrix implements Expectable {
    protected double[][] matrixElements;

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

    public Matrix(double[][] m) {
        setMatrixElements(m);
    }

    public double[][] getMatrixElements() {
        return matrixElements;
    }

    public void setMatrixElements(double[][] matrixElements) throws IllegalArgumentException {
        if (!validateMatrix(matrixElements)) throw new IllegalArgumentException();

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

    public Matrix times(Matrix m) {
        if (getColumns() != m.getRows())
            throw new IllegalArgumentException();

        Matrix toReturn = new Matrix(new double[getRows()][m.getColumns()]);

        for (int i = 0; i < getRows(); i++) {

            for (int j = 0; j < m.getColumns(); j++) {
                double sum = 0;
                for (int k = 0; k < getColumns(); k++) {
                    double firstFactor = matrixElements[i][k];
                    double secondFactor = m.getMatrixElements()[k][j];

                    sum += firstFactor * secondFactor;
                }
                toReturn.set(i, j, sum);
            }

        }

        return toReturn;
    }

    /**
     * Adds to matrices together
     * 
     * @param augend The matrix to add to the current matrix
     * @return The sum of the two matrices
     * @throws IllegalArgumentException When the matrices cannot be added
     */
    public Matrix plus(Matrix augend) {
        if (!(getRows() == augend.getRows() && getColumns() == augend.getColumns()))
            throw new IllegalArgumentException("Cannot add matrices of different dimensions");

        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(i, j, matrixElements[i][j] + augend.getMatrixElements()[i][j]);
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
    public Matrix minus(Matrix minuend) {
        return this.plus(minuend.multiplyByScalar(-1));
    }

    public Matrix multiplyByScalar(double scalar) {
        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(i, j, scalar * matrixElements[i][j]);
            }
        }

        return toReturn;
    }

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
        if (!isSquare())
            throw new MatrixNotInvertibleException();

        int dimensions = getRows();

        double[][] matrixElsToInvert = new double[getRows()][getColumns()];
        for (int i = 0; i < dimensions; i++) {
            matrixElsToInvert[i] = Arrays.copyOf(matrixElements[i], matrixElsToInvert[i].length);
        }

        Matrix matrixToInvert = new Matrix(matrixElsToInvert);
        Matrix augmentedMatrix = createIdentityMatrix(getColumns());
        
        for (int k = 0; k < dimensions; k++) {
            
            double cellValueBeforeOne = matrixToInvert.get(k, k);
            for (int i = 0; i < dimensions; i++) {
                matrixToInvert.set(k, i, matrixToInvert.get(k, i) / cellValueBeforeOne);
                augmentedMatrix.set(k, i, augmentedMatrix.get(k, i) / cellValueBeforeOne);
            }

            for (int i = 0; i < dimensions; i++) {
                double firstZeroFactor = matrixToInvert.get(i, k);
                if (i != k) {
                    for (int j = 0; j < dimensions; j++) {                
                        double firstRowValue = matrixToInvert.get(k, j);
                        double valueToChange = matrixToInvert.get(i, j);

                        matrixToInvert.set(i, j, valueToChange - firstZeroFactor * firstRowValue);
                        
                        firstRowValue = augmentedMatrix.get(k, j);
                        valueToChange = augmentedMatrix.get(i, j);

                        augmentedMatrix.set(i, j, valueToChange - firstZeroFactor * firstRowValue);
                    }
                }
            }
        }
        
        System.out.println(augmentedMatrix);
        
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                augmentedMatrix.set(i, j, roundDouble(augmentedMatrix.get(i, j), 3));
            }
        }

        return augmentedMatrix;
    }

    private static double roundDouble(double d, int places) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static Matrix createIdentityMatrix(int numDimens) {
        double[][] identityElements = new double[numDimens][numDimens];

        for (int i = 0; i < numDimens; i++) {
            for (int j = 0; j < numDimens; j++) {
                identityElements[i][j] = i == j ? 1 : 0;
            }
        }

        return new Matrix(identityElements);
    }

    public static Matrix createDiagonalMatrix(int dimens, double value) {
        double[][] toReturn = new double[dimens][dimens];

        for (int i = 0; i < dimens; i++) {
            
            for (int j = 0; j < dimens; j++) {
                if (i == j)
                    toReturn[i][i] = value;
                else
                    toReturn[i][j] = 0;
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

    public Matrix calcExpectedValue() {
        Matrix expectedValueMatrix = new Matrix(new double[getRows()][getColumns()]);

        // TODO: Calculate expected value

        return expectedValueMatrix;
    }

    public Matrix calcInnerProduct() {
        return this.times(transpose());
    }

    public Matrix calcInnerProduct(Matrix y) {
        return this.transpose().times(y);
    }

    public Matrix calcOuterProduct(Matrix y) {
        return this.times(y.transpose());
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
        return this.times(inverse).isIdentityMatrix() && inverse.times(this).isIdentityMatrix();
    }
}

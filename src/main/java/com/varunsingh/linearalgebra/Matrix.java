package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class Matrix {
    protected double[][] matrixElements;

    /**
     * An exception that is thrown when a matrix is attempted to be inverted
     * when it is not an invertible matrix
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

    public void setMatrixElements(double[][] matrixElements) {
        this.matrixElements = matrixElements;
    }

    public void setMatrixElement(int firstLvlIndex, int secondLvlIndex, double newValue) {
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
                toReturn.setMatrixElement(i, j, sum);
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
                toReturn.setMatrixElement(i, j, matrixElements[i][j] + augend.getMatrixElements()[i][j]);
            }
        }

        return toReturn;
    }

    /**
     * Convenience method for adding to scalar -1 times a matrix
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
                toReturn.setMatrixElement(i, j, scalar * matrixElements[i][j]);        
            }
        }

        return toReturn;
    }
    
    public Matrix transpose() {
        Matrix toReturn = new Matrix(new double[getColumns()][getRows()]);
        
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.setMatrixElement(j, i, matrixElements[i][j]);
            }
        }

        return toReturn;
    }

    /**
     * Inverts the matrix using Gauss-Jordan elimination
     * @return The inverted matrix
     * @throws MatrixNotInvertibleException
     */
    public Matrix invert() throws MatrixNotInvertibleException {
        if (!isSquare())
            throw new MatrixNotInvertibleException();
            
        double[][] matrixElsToInvert = new double[getRows()][getColumns()];
        for (int i = 0; i < 3; i++) {
            matrixElsToInvert[i] = Arrays.copyOf(matrixElements[i], matrixElsToInvert[i].length);
        }
        Matrix matrixToInvert = new Matrix(matrixElsToInvert);

        Matrix augmentedMatrix = getIdentityMatrix(getColumns());

        for (int i = 0; i < getColumns(); i++) {
            // Make first row start with 1
            matrixToInvert.setMatrixElement(0, i, matrixToInvert.get(0, i) / matrixElements[0][0]);
            augmentedMatrix.setMatrixElement(0, i, augmentedMatrix.get(0, i) / augmentedMatrix.get(0, 0));
            
            // Make second row start with 0
            matrixToInvert.setMatrixElement(1, i, matrixToInvert.get(1, i) - matrixElements[1][0] * matrixToInvert.get(0, i));
            augmentedMatrix.setMatrixElement(1, i, augmentedMatrix.get(1, i) - augmentedMatrix.get(1, 0) * matrixToInvert.get(0, i));
            
            // Make third row start with 0
            matrixToInvert.setMatrixElement(2, i, matrixToInvert.get(2, i) - matrixElements[2][0] * matrixToInvert.get(0, i));
            augmentedMatrix.setMatrixElement(2, i, augmentedMatrix.get(2, i) - augmentedMatrix.get(2, 0) * matrixToInvert.get(0, i));

            // 
        }
        
        return augmentedMatrix;
    }
    
    private double get(int row, int col) {
        return matrixElements[row][col];
    }

    public static Matrix getIdentityMatrix(int numDimens) {
        double[][] identityElements = new double[numDimens][numDimens];

        for (int i = 0; i < numDimens; i++) {
            for (int j = 0; j < numDimens; j++) {
                identityElements[i][j] = i == j ? 1 : 0;
            }
        }
        
        return new Matrix(identityElements);
    }

    public double getDeterminant() throws MatrixNotInvertibleException {
        if (!isSquare()) throw new Matrix.MatrixNotInvertibleException();

        if (getRows() == 2) {
            double firstTerm = matrixElements[0][0] * matrixElements[1][1];
            double secondTerm = matrixElements[0][1] * matrixElements[1][0];

            return 1.0 / (firstTerm - secondTerm);
        } else {
            return 0;
        }
    }

    public Matrix getExpectedValue() {
        // TODO: Implement Matrix expected value operation
        throw new UnsupportedOperationException();
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

    public boolean isInverse(Matrix inverse) {
        return this.times(inverse).isIdentityMatrix() && inverse.times(this).isIdentityMatrix();
    }
}

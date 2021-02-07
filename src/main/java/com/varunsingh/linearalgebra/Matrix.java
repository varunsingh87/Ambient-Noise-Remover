package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class Matrix {
    protected double[][] matrixElements;

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

    public Matrix getExpectedValue() {
        return null;
	}

    boolean isSquare() {
        return matrixElements.length == matrixElements[0].length;
    }

    boolean isIdentityMatrix() {
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
}

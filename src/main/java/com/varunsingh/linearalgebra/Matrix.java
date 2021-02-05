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

    public Matrix times(Vector v) {
        if (getColumns() != v.getRows())
            throw new IllegalArgumentException();

        Matrix toReturn = new Matrix(
            new double[getRows()][v.getColumns()]
        );

        for (int i = 0; i < matrixElements.length; i++) {
            double sum = 0;

            for (int j = 0; j < matrixElements[i].length; j++) {
                sum += matrixElements[i][j] * v.getVectorElements()[j];
            }

            toReturn.setMatrixElement(i, 0, sum);
        }

        return toReturn;
    }

    @Override
    public boolean equals(Object matrix) {
        return Arrays.deepEquals(getMatrixElements(), ((Matrix) matrix).getMatrixElements());
    }

    public String toString() {
        return Arrays.deepToString(matrixElements);
    }

    /**
     * Adds to matrices together
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

    boolean isSquare() {
        return matrixElements.length == matrixElements[0].length;
    }

    boolean isIdentityMatrix() {
        if (!isSquare()) return false;

        for (int i = 0; i < getRows(); i++) {
            if (matrixElements[i][i] != 1) return false;
            
            for (int j = 0; j < getColumns(); j++) {
                if (j != i && matrixElements[i][j] != 0)
                    return false;
            }

        }
        
        return true;
    }
}

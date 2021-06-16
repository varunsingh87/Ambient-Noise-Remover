package com.varunsingh.linearalgebra;

import java.util.Arrays;

/**
 * Operation for getting a matrix's inverse using Gauss-Jordan elimination
 */
public class MatrixInverseOperation implements MatrixOperation {
    private Matrix matrixToInvert;
    private Matrix leftMatrix;
    private Matrix augmentedMatrix;
    private int dimensions;

    MatrixInverseOperation(Matrix m) throws Matrix.MatrixNotInvertibleException {
        dimensions = m.getRows();
        matrixToInvert = m;
        leftMatrix = new Matrix(copyMatrixElements(m.getMatrixElements()));
        augmentedMatrix = Matrix.createIdentityMatrix(dimensions);

        if (!matrixToInvert.isSquare()) throw new Matrix.MatrixNotInvertibleException();
    }

    MatrixInverseOperation(double[][] els) throws Matrix.MatrixNotInvertibleException {
        dimensions = els.length;
        matrixToInvert = new Matrix(els);
        leftMatrix = new Matrix(copyMatrixElements(els));
        augmentedMatrix = Matrix.createIdentityMatrix(dimensions);

        if (!matrixToInvert.isSquare()) throw new Matrix.MatrixNotInvertibleException();
    }

    private double[][] copyMatrixElements(double[][] elements) {
        double[][] matrixElsToInvert = new double[dimensions][dimensions];
        
        for (int i = 0; i < dimensions; i++) {
            matrixElsToInvert[i] = Arrays.copyOf(elements[i], dimensions);
        }

        return matrixElsToInvert;
    }

    public Matrix compute() {
        for (int k = 0; k < dimensions; k++) {
            startRowWith1(k); 

            for (int i = 0; i < dimensions; i++)
                if (i != k) startRowWith0(k, i);
        }

        return MatrixRound.roundMatrix(augmentedMatrix, 3);
    }

    private void startRowWith1(int diagonalIndexFromTopLeft) {
        double cellValueBeforeOne = leftMatrix.get(diagonalIndexFromTopLeft, diagonalIndexFromTopLeft);
        for (int i = 0; i < dimensions; i++) {
            leftMatrix.set(
                diagonalIndexFromTopLeft, 
                i, 
                leftMatrix.get(diagonalIndexFromTopLeft, i) / cellValueBeforeOne
            );

            augmentedMatrix.set(
                diagonalIndexFromTopLeft, 
                i, 
                augmentedMatrix.get(diagonalIndexFromTopLeft, i) / cellValueBeforeOne
            );
        }
    }

    private void startRowWith0(int diagonalIndexFromTopLeft, int row) {
        double firstZeroFactor = leftMatrix.get(row, diagonalIndexFromTopLeft);
        for (int j = 0; j < dimensions; j++) {
            double firstRowValue = leftMatrix.get(diagonalIndexFromTopLeft, j);
            double valueToChange = leftMatrix.get(row, j);

            leftMatrix.set(row, j, valueToChange - firstZeroFactor * firstRowValue);

            firstRowValue = augmentedMatrix.get(diagonalIndexFromTopLeft, j);
            valueToChange = augmentedMatrix.get(row, j);

            augmentedMatrix.set(row, j, valueToChange - firstZeroFactor * firstRowValue);
        }
    }

    @Override
    public int getComputedMatrixDimensions() {
        return dimensions;
    }

}

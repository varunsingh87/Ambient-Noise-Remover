package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class MatrixCovarianceOperation implements MatrixOperation {
    private Matrix datasets;

    /**
     * Matrix returned when {@link #compute()} is called has dimensions nxn where n
     * is {@code dimensionsOfComputedMatrix}
     */
    private int dimensionsOfComputedMatrix;

    public MatrixCovarianceOperation(Vector... d) {
        datasets = new Matrix(d);
        dimensionsOfComputedMatrix = datasets.getRows();
    }

    @Override
    public int getComputedMatrixDimensions() {
        return dimensionsOfComputedMatrix;
    }

    @Override
    public Matrix compute() {
        Matrix unity = getUnityMatrix();
        Dataset sums = unity.times(datasets);
            
        Dataset averages = sums.scale((double) 1 / (double) datasets.getRows());

        Matrix deviationMatrix = (Matrix) datasets.minus(averages);

        return (Matrix) deviationMatrix
            .transpose()
            .times(deviationMatrix)
            .scale(Math.pow(datasets.getRows(), -1));
    }

    private Matrix getUnityMatrix() {
        double[][] unityMatrixValues = new double[datasets.getRows()][datasets.getRows()];

        for (int i = 0; i < datasets.getRows(); i++) {
            Arrays.fill(unityMatrixValues[i], 1.0);
        }

        return new Matrix(unityMatrixValues);
    }

}

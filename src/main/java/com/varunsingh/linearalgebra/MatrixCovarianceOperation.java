package com.varunsingh.linearalgebra;

import java.util.Arrays;

public class MatrixCovarianceOperation implements MatrixOperation {
    private Matrix datasets;

    /**
     * Matrix returned when {@link #compute()} is called has dimensions nxn where n
     * is {@code dimensionsOfComputedMatrix}
     */
    private int dimensionsOfComputedMatrix;
    private int sizeOfAnyVector;

    public MatrixCovarianceOperation(Vector... d) {
        datasets = new Matrix(d);
        sizeOfAnyVector = d.length > 0 ? d[0].getSize() : 0;
        dimensionsOfComputedMatrix = datasets.getRows();
    }

    @Override
    public int getComputedMatrixDimensions() {
        return dimensionsOfComputedMatrix;
    }

    @Override
    public Matrix compute() {
        Dataset deviationMatrix = datasets
                .minus(
                    getUnityMatrix()
                        .times(datasets)
                        .scale(1 / dimensionsOfComputedMatrix)
                );

        return (Matrix) deviationMatrix.transpose().times(deviationMatrix);
    }

    private Matrix getUnityMatrix() {
        Matrix unityMatrix = new Matrix(new double[dimensionsOfComputedMatrix][dimensionsOfComputedMatrix]);

        for (int i = 0; i < dimensionsOfComputedMatrix; i++) {
            Arrays.fill(unityMatrix.getMatrixElements()[i], 1.0);
        }

        return unityMatrix;
    }

}

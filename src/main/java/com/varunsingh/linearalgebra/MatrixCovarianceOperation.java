package com.varunsingh.linearalgebra;

public class MatrixCovarianceOperation implements MatrixOperation {
    Vector[] datasets;
    private int dimensionsOfComputedMatrix;

    public MatrixCovarianceOperation(Vector...d) {
        datasets = d;

        if (!checkVectorsAreSameSize()) 
            throw new IllegalArgumentException("Vectors must be the same size for a covariance matrix to be generated");

        dimensionsOfComputedMatrix = calcComputedMatrixDimensions();
    }

    private boolean checkVectorsAreSameSize() {
        int firstVectorSize = datasets[0].getSize();
        for (Vector dataset : datasets) {
            if (dataset.getSize() != firstVectorSize) 
                return false;
        }

        return true;
    }

    @Override
    public int calcComputedMatrixDimensions() {
        return datasets.length;
    }

    @Override
    public Matrix compute() {
        switch (dimensionsOfComputedMatrix) {
            case 1:
                return new Matrix(datasets[0].calcVariance());
            case 2:
                return new Matrix(new double[][] {
                    { datasets[0].calcVariance(), calcVectorCovariance(datasets[0], datasets[1]) },
                    { calcVectorCovariance(datasets[1], datasets[0]), datasets[1].calcVariance() }
                });

            case 3:
                return new Matrix(new double[][] {
                    { datasets[0].calcVariance(), calcVectorCovariance(datasets[0], datasets[1]), calcVectorCovariance(datasets[0], datasets[2]) },
                    { calcVectorCovariance(datasets[1], datasets[0]), datasets[1].calcVariance(), calcVectorCovariance(datasets[1], datasets[2]) },
                    { calcVectorCovariance(datasets[2], datasets[0]), calcVectorCovariance(datasets[2], datasets[1]), datasets[2].calcVariance() }
                });

            default:
                throw new UnsupportedOperationException("That number of dimensions is not supported");
        }
    }

    protected double calcVectorCovariance(Vector v1, Vector v2) {
        double expectedValueOfATimesB = calcVectorProductExpectedValue(v1, v2);
        double productOfExpectedValues_Of_A_And_B = v1.calcAverage() * v2.calcAverage();
        return MatrixRound.roundDouble(expectedValueOfATimesB - productOfExpectedValues_Of_A_And_B, 4);
    }

    protected double calcVectorProductExpectedValue(Vector v1, Vector v2) {
        double sum = 0;
        
        for (int i = 0; i < dimensionsOfComputedMatrix; i++) {
            sum += v1.get(i) * v2.get(i);
        }

        return sum / dimensionsOfComputedMatrix;
    }
}

package com.varunsingh.linearalgebra;

public class MatrixCovarianceOperation implements MatrixOperation {
    Vector[] datasets;
    private int dimensionsOfComputedMatrix;
    private int sizeOfAnyVector;

    public MatrixCovarianceOperation(Vector...d) {
        datasets = d;

        if (!checkVectorsAreSameSize()) 
            throw new IllegalArgumentException("Vectors must be the same size for a covariance matrix to be generated");
        sizeOfAnyVector = d.length > 0 ? d[0].getSize() : 0;

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
                    { datasets[0].calcVariance(), calcVectorCovariance(0, 1) },
                    { calcVectorCovariance(1, 0), datasets[1].calcVariance() }
                });

            case 3:
                return new Matrix(new double[][] {
                    { datasets[0].calcVariance(), calcVectorCovariance(0, 1), calcVectorCovariance(0, 2) },
                    { calcVectorCovariance(1, 0), datasets[1].calcVariance(), calcVectorCovariance(1, 2) },
                    { calcVectorCovariance(2, 0), calcVectorCovariance(2, 1), datasets[2].calcVariance() }
                });

            default:
                throw new UnsupportedOperationException("That number of dimensions is not supported");
        }
    }

    protected double calcVectorCovariance(int index1, int index2) {
        double covarianceSum = 0;
        System.out.println(String.format("Sigma-%01d * Sigma-%01d = [0 ", index1, index2));

        for (int i = 0; i < sizeOfAnyVector; i++) {
            double firstVectorDeviation = Math.abs(datasets[index1].getAverage() - datasets[index1].get(i));
            
            double secondVectorDeviation = Math.abs(datasets[index2].getAverage() - datasets[index2].get(i));

            covarianceSum += firstVectorDeviation * secondVectorDeviation;

            System.out.println(String.format("+ (%f - %f)(%f - %f)", datasets[index1].getAverage(), datasets[index1].get(i), datasets[index2].getAverage(), datasets[index2].get(i)));
        }

        System.out.print("]");
        
        return MatrixRound.roundDouble(covarianceSum / sizeOfAnyVector, 5);
    }
}

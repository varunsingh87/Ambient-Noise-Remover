package com.varunsingh.linearalgebra;

public class MatrixCovarianceOperation implements MatrixOperation {
    Vector vector1;
    Vector vector2;

    public MatrixCovarianceOperation(Vector v1, Vector v2) {
        vector1 = v1;
        vector2 = v2;
    }

    @Override
    public Matrix compute() {
        return new Matrix(new double[][] {
            { calcVectorCovariance(vector1, vector1), calcVectorCovariance(vector1, vector2) },
            { calcVectorCovariance(vector2, vector1), calcVectorCovariance(vector2, vector2) }
        });
    }

    protected double calcVectorCovariance(Vector v1, Vector v2) {
        double expectedValueOfATimesB = calcVectorProductExpectedValue(v1, v2);
        double productOfExpectedValues_Of_A_And_B = v1.calcExpectedValue() * v2.calcExpectedValue();
        return expectedValueOfATimesB - productOfExpectedValues_Of_A_And_B;
    }

    protected double calcVectorProductExpectedValue(Vector v1, Vector v2) {
        double sum = 0;
        
        for (int i = 0; i < Math.min(v1.getRows(), v2.getRows()); i++) {
            sum += v1.get(i) * v2.get(i);
        }

        return sum / Math.min(v1.getRows(), v2.getRows());
    }
}

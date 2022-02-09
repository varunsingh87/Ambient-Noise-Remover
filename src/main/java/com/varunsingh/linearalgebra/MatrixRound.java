package com.varunsingh.linearalgebra;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MatrixRound {
    public static Matrix roundMatrix(Matrix unroundedMatrix, int decimalPlaces) {
        Matrix roundedMatrix = new Matrix(
            new double[unroundedMatrix.getRows()][unroundedMatrix.getColumns()]
        );

        for (int i = 0; i < roundedMatrix.getRows(); i++) {
            for (int j = 0; j < roundedMatrix.getColumns(); j++) {
                roundedMatrix.set(
                    i, j, roundDouble(unroundedMatrix.get(i, j), decimalPlaces)
                );
            }
        }

        return roundedMatrix;
    }

    public static Vector roundVector(Vector unroundedVector, int decimalPlaces) {
        Vector roundedVector = new Vector(
            new double[unroundedVector.getSize()]
        );

        for (int i = 0; i < roundedVector.getSize(); i++) {
            roundedVector.set(
                i, roundDouble(unroundedVector.get(i), decimalPlaces)
            );
        }

        return roundedVector;
    }

    static double roundDouble(double d, int places) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }
}

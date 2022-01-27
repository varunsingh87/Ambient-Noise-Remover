package com.varunsingh.linearalgebra;

import java.util.Arrays;

import com.varunsingh.linearalgebra.Vector.VectorType;

public class Matrix implements Dataset {
    protected double[][] matrixElements;
    protected Vector[] vectorRows;
    protected Vector[] vectorColumns;

    /**
     * An exception that is thrown when a matrix is attempted to be inverted when it
     * is not an invertible matrix
     */
    public final static class MatrixNotInvertibleException extends Exception {
        private static final long serialVersionUID = -5031211581058588672L;

        MatrixNotInvertibleException() {
            super("The matrix must be square to calculate the inverse");
        }
    }

    public Matrix() {
    }

    public Matrix(int rows, int columns) {
        setMatrixElements(new double[rows][columns]);
    }

    public Matrix(double scalar) {
        setMatrixElements(new double[][] { { scalar } });
    }

    /**
     * Constructs a Matrix using a two-dimensional array
     * @param m The two-dimensional array containing all the matrix elements
     */
    public Matrix(double[][] m) {
        vectorRows = new Vector[m.length];
        vectorColumns = new Vector[m[0].length];
        setMatrixElements(m);
    }

    /**
     * Constructs a matrix by row vectors
     * @param rows The set of vectors that are merged together to create a matrix
     * @return A, the Matrix object
     */
    public Matrix(Vector[] rows) {
        matrixElements = new double[rows.length][rows[0].getSize()];
        
        for (int i = 0; i < rows.length; i++) {
            matrixElements[i] = rows[i].getValues();
        }

        vectorRows = rows;
        vectorColumns = computeVectorColumns(matrixElements);
    }

    public Matrix(Vector v) {
        if (v.getOrientation() == VectorType.ROW) {
            matrixElements = new double[][] { v.getValues()};
            vectorRows = new Vector[] { v };
            vectorColumns = computeVectorColumns(matrixElements);
        } else {
            matrixElements = new double[v.getSize()][1];
            for (int i = 0; i < v.getSize(); i++) {
                set(i, 0, v.get(i));
            }
            vectorColumns = new Vector[] { v };
        }
    }

    public double[][] getMatrixElements() {
        return matrixElements;
    }

    public void setMatrixElements(double[][] matrixElements) throws IllegalArgumentException {
        if (!validateMatrix(matrixElements)) throw new IllegalArgumentException();

        this.vectorRows = computeVectorRows(matrixElements);
        this.vectorColumns = computeVectorColumns(matrixElements);
        this.matrixElements = matrixElements;
    }

    private Vector[] computeVectorRows(double[][] matrixElements) {
        Vector[] vectorRows = new Vector[matrixElements.length];
        
        for (int i = 0; i < matrixElements.length; i++) {
            vectorRows[i] = Vector.row(matrixElements[i]);
        }

        return vectorRows;
    }

    private Vector[] computeVectorColumns(double[][] matrixElements) {
        Vector[] vectorColumns = new Vector[matrixElements[0].length];
        for (int i = 0; i < vectorColumns.length; i++) {
            Vector column = new Vector(matrixElements.length, VectorType.COLUMN);
            for (int j = 0; j < matrixElements.length; j++) {
                column.set(j, matrixElements[j][i]);
            }
            vectorColumns[i] = column;
        }

        return vectorColumns;
    }

    public static boolean validateMatrix(double[][] elsToValidate) {
        if (elsToValidate.length < 1) return true;
        
        int columns = elsToValidate[0].length;
        for (int i = 0; i < elsToValidate.length; i++) {
            if (columns != elsToValidate[i].length)
                return false;
        }

        return true;
    }

    public double get(int row, int col) {
        return matrixElements[row][col];
    }

    public Vector getRow(int row) {
        return vectorRows[row];
    }

    public Vector getColumn(int column) {
        return vectorColumns[column];
    }

    public void set(int firstLvlIndex, int secondLvlIndex, double newValue) {
        matrixElements[firstLvlIndex][secondLvlIndex] = newValue;
        
        if (vectorRows[firstLvlIndex] != null) {
            vectorRows[firstLvlIndex].set(secondLvlIndex, newValue);
        }
        
        if (vectorColumns[secondLvlIndex] != null) {
            vectorColumns[secondLvlIndex].set(firstLvlIndex, newValue);
        }
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

    public Dataset times(Dataset d) {
        if (d instanceof Vector) {
            return timesVector((Vector) d);
        } else if (d instanceof Matrix) {
            return timesMatrix((Matrix) d);
        } else {
            throw new IllegalArgumentException("The argument must be a Vector or Matrix");
        }
    }

    public Dataset timesMatrix(Matrix d) {
        if (getColumns() != d.getRows())
            throw new IllegalArgumentException();

        Matrix product = new Matrix(getRows(), d.getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < d.getColumns(); j++) {
                product.set(i, j, vectorRows[i].dot(d.getColumn(j)));
            }
        }

        return product;
    }

    /**
     * Multiply a matrix by a column vector
     * @param d The vector by which the matrix is multiplied
     * @return The product of the matrix and the vector (getRows() x 1 column vector)
     */
    public Dataset timesVector(Vector d) {
        if (getColumns() != d.getSize())
            throw new IllegalArgumentException("The number of columns in the matrix must equal the number of elements in the vector");

        Vector result = new Vector(getRows(), VectorType.COLUMN);
        
        for (int i = 0; i < getRows(); i++) {
            result.set(i, vectorRows[i].dot(d));
        }

        return result;
    }

    /**
     * Adds two matrices together
     * 
     * @param addend The matrix to add to the current matrix
     * @return The sum of the two matrices
     * @throws IllegalArgumentException When the matrices cannot be added
     */
    public Matrix plus(Dataset addend) {
        if (!(getRows() == addend.getRows() && getColumns() == addend.getColumns()))
            throw new IllegalArgumentException("Cannot add matrices of different dimensions");

        Matrix mAddend = (Matrix) addend;
        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(
                    i, 
                    j, 
                    matrixElements[i][j] + mAddend.get(i, j)
                );
            }
        }

        return toReturn;
    }

    /**
     * Convenience method for adding to scalar -1 times a matrix
     * 
     * @param minuend The matrix being subtracted
     * @return The difference matrix
     */
    public Dataset minus(Dataset minuend) {
        return this.plus(minuend.scale(-1));
    }

    public Dataset divide(Matrix denominator) throws MatrixNotInvertibleException {
        return times(denominator.invert());
    }

    @Override
    public Matrix scale(double scalar) {
        Matrix toReturn = new Matrix(new double[getRows()][getColumns()]);

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(i, j, MatrixRound.roundDouble(scalar * matrixElements[i][j], 5));
            }
        }

        return toReturn;
    }

    @Override
    public Matrix transpose() {
        Matrix toReturn = new Matrix(getColumns(), getRows());

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                toReturn.set(j, i, matrixElements[i][j]);
            }
        }

        return toReturn;
    }

    /**
     * Inverts the matrix using Gauss-Jordan elimination
     * 
     * @return The inverted matrix
     * @throws MatrixNotInvertibleException
     */
    public Matrix invert() throws MatrixNotInvertibleException {
        return new MatrixInverseOperation(this).compute();
    }

    public static Matrix createIdentityMatrix(int size) {
        double[][] identityElements = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                identityElements[i][j] = i == j ? 1 : 0;
            }
        }

        return new Matrix(identityElements);
    }

    public static Matrix createDiagonalMatrix(int dimens, double value) {
        double[][] toReturn = new double[dimens][dimens];

        for (int i = 0; i < dimens; i++) {
            
            for (int j = 0; j < dimens; j++) {
                if (i == j) toReturn[i][i] = value;
                else toReturn[i][j] = 0;
            }
        }

        return new Matrix(toReturn);
    }

    public Matrix zeroOutMinorDiagonal() {
        if (!isSquare())
            throw new IllegalArgumentException("Matrix must be square");

        for (int i = 0; i < getRows(); i++) {
            set(getRows() - i - 1, i, 0);
        }

        return this;
    }

    public double getDeterminant() throws MatrixNotInvertibleException {
        if (!isSquare())
            throw new Matrix.MatrixNotInvertibleException();

        if (getRows() == 2) {
            double firstTerm = matrixElements[0][0] * matrixElements[1][1];
            double secondTerm = matrixElements[0][1] * matrixElements[1][0];

            return 1.0 / (firstTerm - secondTerm);
        } else {
            return 0;
        }
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

    public boolean isDiagonal() throws IllegalArgumentException {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                if (i != j && matrixElements[i][j] != 0) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public boolean isInverse(Matrix inverse) {
        return ((Matrix) this.times(inverse)).isIdentityMatrix() 
            && ((Matrix) inverse.times(this)).isIdentityMatrix();
    }

    public Vector asRowVector() {
        return Vector.row(this.getMatrixElements()[0]);
    }

    public Vector asColumnVector() {
        return Vector.valueOf(this);
    }

    public boolean isConventionalMatrix() {
        return getRows() > 1 && getColumns() > 1;
    }
}

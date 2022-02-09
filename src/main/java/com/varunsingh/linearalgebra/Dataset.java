package com.varunsingh.linearalgebra;

/**
 * A set of double values
 */
public interface Dataset {
    int getRows();

    int getColumns();

    Dataset times(Dataset factor);

    Dataset plus(Dataset addend);

    Dataset minus(Dataset minuend);

    Dataset transpose();

    Dataset scale(double scalar);
}

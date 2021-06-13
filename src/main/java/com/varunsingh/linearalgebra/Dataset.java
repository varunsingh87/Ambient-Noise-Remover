package com.varunsingh.linearalgebra;

/**
 * A one-dimensional set of double values
 */
public interface Dataset {
    /**
     * Gets the elements in the dataset
     * @return The elements in a double array
     */
    public double[] getDatasetElements();

    /**
     * Around 100% of values are between positive and negative sigma squared
     * @return The positive variance (Sigma^2)
     */
    public double calcVariance();

    /**
     * Around 68.3% of all values are between positive and negative sigma
     * @return The positive standard deviation (Sigma)
     */
    public double calcStandardDeviation();
}

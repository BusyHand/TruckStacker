package ru.liga.truck_box_stacker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing different types of algorithms for box stacking.
 * Each type has an associated maximum size of data that it can handle.
 */
@AllArgsConstructor
public enum TypeAlgorithm {

    NOT_A_LOT_OF_DATA(5000), // Algorithm for small data sizes.
    A_LOT_OF_DATA(Integer.MAX_VALUE); // Algorithm for larger data sizes.

    @Getter
    private int maxSizeOfDataToUseAlgorithm; // Maximum size of data for the algorithm.
}

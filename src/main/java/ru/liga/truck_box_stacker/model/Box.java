package ru.liga.truck_box_stacker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a box with specific dimensions and a visual representation.
 * Each box has a height, width, and a pattern represented as a two-dimensional array.
 */
@Data
@AllArgsConstructor
public class Box {

    private final int height;                 // Height of the box.
    private final int width;                  // Width of the box.
    private final String stringInterpretation; // String representation for display purposes.
    private final int[][] boxPattern;         // 2D array representing the visual pattern of the box.
}

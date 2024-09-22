package ru.liga.truckstacker.model;

/**
 * Represents a box with specific dimensions and a visual representation.
 * Each box has a height, width, and a pattern represented as a two-dimensional array.
 */

public record Box(
        int height,
        int width,
        String representation,
        int[][] boxPattern) {
}
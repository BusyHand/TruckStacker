package ru.liga.truck_box_stacker.model;

import lombok.AllArgsConstructor;

/**
 * Enum representing command-line argument names.
 * Currently, it only contains a path argument for input files.
 */
@AllArgsConstructor
public enum CommandLineArgs {
    PATH_INPUT_FILE("path"); // Command line argument for the input file path.

    private String name; // Name of the command line argument.
}

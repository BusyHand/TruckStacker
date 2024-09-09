package ru.liga.truck_box_stacker.config.bpp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation indicating that a field in a bean should be injected
 * with the path to the input file from command-line arguments.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BoxInputFile {
}

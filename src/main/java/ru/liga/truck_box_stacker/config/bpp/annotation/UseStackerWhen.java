package ru.liga.truck_box_stacker.config.bpp.annotation;

import ru.liga.truck_box_stacker.model.TypeAlgorithm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to associate a bean with a specific TypeAlgorithm.
 * This is useful for configuring beans that rely on different
 * algorithms depending on the type.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UseStackerWhen {
    TypeAlgorithm value(); // Defines the TypeAlgorithm for this annotation.
}

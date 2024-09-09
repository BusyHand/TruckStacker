package ru.liga.truck_box_stacker.config.bpp.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation indicating that a field in a bean should be automatically
 * injected with a mapping of TypeAlgorithm to bean names for that bean.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AutowiredTypeAlgorithmToBeanName {
}

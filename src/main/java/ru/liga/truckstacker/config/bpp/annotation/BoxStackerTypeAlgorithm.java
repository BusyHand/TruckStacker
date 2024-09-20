package ru.liga.truckstacker.config.bpp.annotation;

import ru.liga.truckstacker.config.bpp.TypeAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BoxStackerTypeAlgorithm {
    TypeAlgorithm value();
}

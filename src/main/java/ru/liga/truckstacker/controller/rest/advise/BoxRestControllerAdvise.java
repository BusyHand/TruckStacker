package ru.liga.truckstacker.controller.rest.advise;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BoxRestControllerAdvise {

    @ExceptionHandler(IllegalArgumentException.class)
    public String getHandle(IllegalArgumentException e) {
        return "ERORR";
    }
}

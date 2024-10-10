package ru.liga.truckstacker.exception;

public class BoxNotFoundException extends RuntimeException {

    public BoxNotFoundException(String message) {
        super(message);
    }
}

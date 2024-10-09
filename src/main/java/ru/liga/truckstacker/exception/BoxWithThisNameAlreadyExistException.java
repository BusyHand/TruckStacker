package ru.liga.truckstacker.exception;

public class BoxWithThisNameAlreadyExistException extends RuntimeException {

    public BoxWithThisNameAlreadyExistException(String message) {
        super(message);
    }
}
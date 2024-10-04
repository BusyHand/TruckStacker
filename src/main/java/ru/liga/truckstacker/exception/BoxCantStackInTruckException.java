package ru.liga.truckstacker.exception;

public class BoxCantStackInTruckException extends RuntimeException {

    public BoxCantStackInTruckException(String message) {
        super(message);
    }
}
package ru.liga.truckstacker.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.liga.truckstacker.exception.BoxCantStackInTruckException;
import ru.liga.truckstacker.exception.BoxNotFoundException;

@RestControllerAdvice
public class RestControllerExceptionHandlerAdvise {

    @ExceptionHandler(BoxNotFoundException.class)
    public ResponseEntity<String> handleBoxNotFoundException(BoxNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Коробка с таким именем не найдена: " + ex.getMessage());
    }

    @ExceptionHandler(BoxCantStackInTruckException.class)
    public ResponseEntity<String> handleBoxCantStackInTruckException(BoxCantStackInTruckException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Коробка не может уместиться: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Не корректный аргумент: " + ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Произошла ошибка: " + ex.getMessage());
    }
}

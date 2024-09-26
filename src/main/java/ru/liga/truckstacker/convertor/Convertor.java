package ru.liga.truckstacker.convertor;

public interface Convertor<T, R> {

    R convert(T t);
}

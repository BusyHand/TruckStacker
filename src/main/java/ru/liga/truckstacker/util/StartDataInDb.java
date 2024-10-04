package ru.liga.truckstacker.util;

import lombok.Getter;
import ru.liga.truckstacker.model.Box;

/**
 * Перечисление, представляющее типы коробок, которые могут использоваться в укладке.
 * Каждый тип коробки имеет заданную высоту, ширину, строковое представление и шаблон,
 * который определяет, как коробка выглядит внутри грузовика.
 */
public enum StartDataInDb {

    ONE("один", "1\n", '1'),
    TWO("два", "22\n", '2'),
    THREE("три", "333\n", '3'),
    FOUR("четыре", "4444\n", '4'),
    FIVE("пять", "55555\n", '5'),
    SIX("шесть", "666\n666\n", '6'),
    SEVEN("семь", "777 \n7777\n", '7'),
    EIGHT("восемь", "8888\n8888\n", '8'),
    NINE("девять", "999\n999\n999\n", '9');

    @Getter
    private final Box box;

    StartDataInDb(String name, String form, char symbol) {
        this.box = new Box(name, form, symbol);
    }
}

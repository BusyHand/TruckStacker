package ru.liga.truckstacker.util;

import lombok.Getter;
import ru.liga.truckstacker.entity.Box;

/**
 * Перечисление, представляющее типы коробок, которые могут использоваться в укладке.
 * Каждый тип коробки имеет заданную высоту, ширину, строковое представление и шаблон,
 * который определяет, как коробка выглядит внутри грузовика.
 */
public enum BoxTestData {

    ONE("один", "1", '1'),
    TWO("два", "22", '2'),
    THREE("три", "333", '3'),
    FOUR("четыре", "4444", '4'),
    FIVE("пять", "55555", '5'),
    SIX("шесть", "666\n666", '6'),
    SEVEN("семь", "777 \n7777", '7'),
    EIGHT("восемь", "8888\n8888", '8'),
    NINE("девять", "999\n999\n999", '9');

    @Getter
    private final Box box;

    BoxTestData(String name, String form, char symbol) {
        this.box = new Box(name, form, symbol);
    }
}

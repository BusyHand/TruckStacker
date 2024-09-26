package ru.liga.truckstacker.model;

import lombok.Getter;
import ru.liga.truckstacker.model.Box;
/**
 * Перечисление, представляющее типы коробок, которые могут использоваться в укладке.
 * Каждый тип коробки имеет заданную высоту, ширину, строковое представление и шаблон,
 * который определяет, как коробка выглядит внутри грузовика.
 */
public enum BoxType {

    ONE(1, 1, "1\n", new int[][]{{1}}),
    TWO(1, 2, "22\n", new int[][]{{2, 2}}),
    THREE(1, 3, "333\n", new int[][]{{3, 3, 3}}),
    FOUR(1, 4, "4444\n", new int[][]{{4, 4, 4, 4}}),
    FIVE(1, 5, "55555\n", new int[][]{{5, 5, 5, 5, 5}}),
    SIX(2, 3, "666\n666\n", new int[][]{{6, 6, 6}, {6, 6, 6}}),
    SEVEN(2, 4, "777\n7777\n", new int[][]{{7, 7, 7, 7}, {7, 7, 7, 0}}),
    EIGHT(2, 4, "8888\n8888\n", new int[][]{{8, 8, 8, 8}, {8, 8, 8, 8}}),
    NINE(3, 3, "999\n999\n999\n", new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}});

    @Getter
    private Box box;

    BoxType(int height, int width, String stringInterpretation, int[][] boxPattern) {
        this.box = new Box(height, width, stringInterpretation, boxPattern);
    }
}

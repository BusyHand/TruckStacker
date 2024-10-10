package ru.liga.truckstacker.service.stacker.impl.balanced.model;

import ru.liga.truckstacker.entity.Box;

import static ru.liga.truckstacker.config.AppGlobalSettings.EMPTY_CELL;

/**
 * Класс, представляющий статистику площади коробки.
 * Этот класс хранит информацию о площади, занятой коробкой.
 */
public class AreaStatBox implements Comparable<AreaStatBox> {

    private final Box box;

    private final int area;

    public AreaStatBox(Box box) {
        this.box = box;
        this.area = calculateArea(box.getBoxPattern());
    }

    private int calculateArea(char[][] boxPattern) {
        int area = 0;
        for (char[] row : boxPattern) {
            for (char cell : row) {
                if (cell != EMPTY_CELL) {
                    area++;
                }
            }
        }
        return area;
    }


    public int getArea() {
        return area;
    }

    @Override
    public int compareTo(AreaStatBox other) {
        return Integer.compare(this.area, other.area);
    }

    public Box getBox() {
        return box;
    }

}

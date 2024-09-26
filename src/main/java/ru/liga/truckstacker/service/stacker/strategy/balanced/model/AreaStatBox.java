package ru.liga.truckstacker.service.stacker.strategy.balanced.model;

import ru.liga.truckstacker.model.Box;
/**
 * Класс, представляющий статистику площади коробки.
 * Этот класс хранит информацию о площади, занятой коробкой.
 */
public class AreaStatBox implements Comparable<AreaStatBox> {

    private final Box box;

    private final int AREA;

    public AreaStatBox(Box box) {
        this.box = box;
        this.AREA = calculateArea(box.boxPattern());
    }

    private int calculateArea(int[][] boxPattern) {
        int area = 0;
        for (int[] row : boxPattern) {
            for (int cell : row) {
                if (cell != 0) {
                    area++;
                }
            }
        }
        return area;
    }


    public int getArea() {
        return AREA;
    }

    @Override
    public int compareTo(AreaStatBox other) {
        return Integer.compare(this.AREA, other.AREA);
    }

    public Box getBox() {
        return box;
    }

}

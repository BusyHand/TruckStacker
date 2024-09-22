package ru.liga.truckstacker.service.stacker.impl.balanced.model;

import ru.liga.truckstacker.model.Box;

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
                if (cell != 0) { // Count only non-zero cells
                    area++;
                }
            }
        }
        return area;
    }

    public Box getBox() {
        return box;
    }

    public int getArea() {
        return AREA;
    }

    @Override
    public int compareTo(AreaStatBox other) {
        return Integer.compare(this.AREA, other.AREA);
    }


}

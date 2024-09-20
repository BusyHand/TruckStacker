package ru.liga.truckstacker.service.stacker.impl.burke.model;

import lombok.Data;
import ru.liga.truckstacker.model.Box;

/**
 * Helper class for handling height statistics for boxes.
 */
@Data
public class HeightStatBox implements Comparable<HeightStatBox> {
    private final Box box;
    private int[] heightStat;

    public HeightStatBox(Box box) {
        this.box = box;
        this.calculateHeightStat();
    }

    private void calculateHeightStat() {
        int[][] boxPattern = this.box.boxPattern();
        this.heightStat = new int[boxPattern[0].length];

        for (int j = 0; j < boxPattern[0].length; j++) {
            int height = boxPattern[boxPattern.length - 1][j] == 0
                    ? boxPattern.length - 1
                    : boxPattern.length;
            this.heightStat[j] += height;
        }

    }

    @Override
    public int compareTo(HeightStatBox o) {
        Box otherBox = o.getBox();
        return Integer.compare(this.box.width(), otherBox.width());
    }
}

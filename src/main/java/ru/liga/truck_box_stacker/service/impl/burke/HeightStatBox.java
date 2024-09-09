
package ru.liga.truck_box_stacker.service.impl.burke;

import lombok.Data;
import ru.liga.truck_box_stacker.model.Box;
/**
 * Helper class for handling height statistics for boxes.
 */
@Data
class HeightStatBox implements Comparable<HeightStatBox> {
    private final Box box;
    private int[] heightStat;

    public HeightStatBox(Box box) {
        this.box = box;
        this.calculateHeightStat();
    }

    private void calculateHeightStat() {
        int[][] boxPattern = this.box.getBoxPattern();
        this.heightStat = new int[boxPattern[0].length];

        for (int j = 0; j < boxPattern[0].length; ++j) {
            if (boxPattern[boxPattern.length - 1][j] == 0) {
                this.heightStat[j] = this.heightStat[j] + boxPattern.length - 1;
            } else {
                this.heightStat[j] += boxPattern.length;
            }
        }

    }

    public int compareTo(HeightStatBox o) {
        Box otherBox = o.getBox();
        int thisWidth = this.box.getWidth();
        int otherWidth = otherBox.getWidth();
        if (thisWidth == otherWidth) {
            return 0;
        } else {
            return this.box.getWidth() > otherBox.getWidth() ? -1 : 1;
        }
    }
}

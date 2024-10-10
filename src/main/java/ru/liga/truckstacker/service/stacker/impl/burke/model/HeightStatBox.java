package ru.liga.truckstacker.service.stacker.impl.burke.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.liga.truckstacker.entity.Box;

import static ru.liga.truckstacker.config.AppGlobalSettings.EMPTY_CELL;

/**
 * Класс, представляющий статистику высоты коробки.
 * <p>
 * Этот класс хранит информацию о высоте коробки и реализует интерфейс
 * Comparable для возможности сравнения коробок по ширине.
 */
@Getter
@EqualsAndHashCode
public class HeightStatBox implements Comparable<HeightStatBox> {
    private final Box box;
    @EqualsAndHashCode.Exclude
    private int[] heightStat;

    public HeightStatBox(Box box) {
        this.box = box;
        this.calculateHeightStat();
    }

    private void calculateHeightStat() {
        char[][] boxPattern = this.box.getBoxPattern();
        this.heightStat = new int[boxPattern[0].length];

        for (int j = 0; j < boxPattern[0].length; j++) {
            int height = boxPattern[boxPattern.length - 1][j] == EMPTY_CELL
                    ? boxPattern.length - 1
                    : boxPattern.length;
            this.heightStat[j] += height;
        }

    }

    @Override
    public int compareTo(HeightStatBox o) {
        Box otherBox = o.getBox();
        return Integer.compare(this.box.getWidth(), otherBox.getWidth());
    }
}

package ru.liga.truckstacker.service.stacker.strategy.burke.model;

import lombok.Getter;
import ru.liga.truckstacker.config.GlobalSettings;
import ru.liga.truckstacker.model.Truck;

import java.util.Arrays;

/**
 * Класс, представляющий статистику высоты грузовика.
 *
 * Этот класс хранит информацию о высоте каждого столбца в грузовике.
 */
@Getter
public class HeightStatTruck {
    private final Truck truck;
    private int[] heightStat;

    public HeightStatTruck() {
        this(new Truck());
    }

    public HeightStatTruck(Truck truck) {
        this.truck = truck;
        init();
    }

    private void init() {
        this.heightStat = new int[this.truck.getTruckArea()[0].length];
        calculateInitialHeights();
    }


    private void calculateInitialHeights() {
        int[][] truckArea = this.truck.getTruckArea();
        int truckHeight = truckArea.length;

        for (int x = 0; x < truckArea[0].length; x++) {
            int currentHeight = 0;

            for (int y = truckHeight - 1; y >= 0; y--) {
                if (truckArea[y][x] == 0) {
                    currentHeight++;
                }
                heightStat[x] = truckHeight - currentHeight;
            }
        }
    }

    /**
     * Проверяет, заполнен ли грузовик на максимальную высоту.
     * @return true, если грузовик заполнен; иначе false.
     */
    public boolean isFull() {
        int sum = Arrays.stream(heightStat).sum();
        return sum == GlobalSettings.HEIGHT_TRUCK * GlobalSettings.WIDTH_TRUCK;
    }
}

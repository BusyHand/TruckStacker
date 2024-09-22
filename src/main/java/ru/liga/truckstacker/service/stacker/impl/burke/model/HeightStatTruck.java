package ru.liga.truckstacker.service.stacker.impl.burke.model;

import lombok.Data;
import ru.liga.truckstacker.config.GlobalSettings;
import ru.liga.truckstacker.model.Truck;

import java.util.Arrays;

/**
 * Helper class for handling height statistics for trucks.
 */
@Data
public class HeightStatTruck {
    private final Truck truck;
    private int[] heightStat;

    public HeightStatTruck() {
        this.truck = new Truck();
        init();
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
        for (int x = 0; x < truckArea[0].length; x++) {
            for (int y = 0; y < truckArea.length; y++) {
                if (this.truck.getTruckArea()[y][x] != 0) {
                    heightStat[x] = Math.max(heightStat[x], y + 1);
                }
            }
        }
    }

    public boolean isFull() {
        int sum = Arrays.stream(heightStat).sum();
        return sum == GlobalSettings.HEIGHT_TRUCK * GlobalSettings.WIDTH_TRUCK;
    }
}

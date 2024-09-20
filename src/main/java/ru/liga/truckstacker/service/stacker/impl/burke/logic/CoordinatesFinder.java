package ru.liga.truckstacker.service.stacker.impl.burke.logic;

import ru.liga.truckstacker.service.stacker.impl.burke.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

public class CoordinatesFinder {
    public CoordinatesInTruckArea findMinHeightCoordinates(HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int x = 0;
        int y = Integer.MAX_VALUE;

        for (int i = 0; i < heightStat.length; i++) {
            if (y > heightStat[i]) {
                x = i;
                y = heightStat[i];
            }
        }

        return new CoordinatesInTruckArea(x, y);
    }
}

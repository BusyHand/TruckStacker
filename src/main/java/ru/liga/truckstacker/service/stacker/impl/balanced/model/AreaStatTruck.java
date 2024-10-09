package ru.liga.truckstacker.service.stacker.impl.balanced.model;

import lombok.Getter;
import lombok.Setter;
import ru.liga.truckstacker.entity.Truck;

import static ru.liga.truckstacker.util.GlobalSettings.EMPTY_CELL;

/**
 * Класс, представляющий статистику свободной площади грузовика.
 */
@Getter
@Setter
public class AreaStatTruck implements Comparable<AreaStatTruck> {

    private final Truck truck;
    private int freeArea;

    public AreaStatTruck(Truck truck) {
        this.truck = truck;
        this.freeArea = calculateFreeArea(truck.getTruckArea());
    }

    private int calculateFreeArea(char[][] truckArea) {
        int area = 0;
        for (char[] row : truckArea) {
            for (char cell : row) {
                if (cell == EMPTY_CELL) {
                    area++;
                }
            }
        }
        return area;
    }



    @Override
    public int compareTo(AreaStatTruck other) {
        return Integer.compare(this.freeArea, other.getFreeArea());
    }


}

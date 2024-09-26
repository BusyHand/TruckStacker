package ru.liga.truckstacker.service.stacker.strategy.balanced.model;

import lombok.Getter;
import lombok.Setter;
import ru.liga.truckstacker.model.Truck;
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

    private int calculateFreeArea(int[][] truckArea) {
        int area = 0;
        for (int[] row : truckArea) {
            for (int cell : row) {
                if (cell == 0) {
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

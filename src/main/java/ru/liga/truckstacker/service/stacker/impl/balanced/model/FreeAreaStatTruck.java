package ru.liga.truckstacker.service.stacker.impl.balanced.model;

import lombok.Getter;
import lombok.Setter;
import ru.liga.truckstacker.model.Truck;

@Getter
@Setter
public class FreeAreaStatTruck implements Comparable<FreeAreaStatTruck> {

    private final Truck truck;
    private int freeArea;

    public FreeAreaStatTruck(Truck truck) {
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

    public Truck getTruck() {
        return truck;
    }

    @Override
    public int compareTo(FreeAreaStatTruck other) {
        return Integer.compare(this.freeArea, other.getFreeArea());
    }
}

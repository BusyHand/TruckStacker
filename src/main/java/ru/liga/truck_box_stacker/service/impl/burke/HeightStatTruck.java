
package ru.liga.truck_box_stacker.service.impl.burke;

import lombok.Data;
import ru.liga.truck_box_stacker.config.GlobalSettings;
import ru.liga.truck_box_stacker.model.Truck;

import java.util.Arrays;

/**
 * Helper class for handling height statistics for trucks.
 */
@Data
class HeightStatTruck {
    private final Truck truck = new Truck();
    private int[] heightStat;

    HeightStatTruck() {
        this.heightStat = new int[this.truck.getTruckArea()[0].length];
    }

    public boolean isFull() {
        int sum = Arrays.stream(this.heightStat).sum();
        return sum == GlobalSettings.HEIGHT_TRUCK * GlobalSettings.WIDTH_TRUCK;
    }
}

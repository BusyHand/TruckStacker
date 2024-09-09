
package ru.liga.truck_box_stacker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static ru.liga.truck_box_stacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truck_box_stacker.config.GlobalSettings.WIDTH_TRUCK;
/**
 * Represents a list of trucks.
 * Provides a method to display the trucks in a formatted string.
 */
@Data
@AllArgsConstructor
public class TruckList {
    private List<? extends Truck> truckList;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (Truck truck : truckList) {
            sb.append("Truck ").append(count++).append('\n');
            int[][] truckArea = truck.getTruckArea();
            for (int i = HEIGHT_TRUCK - 1; i > -1; i--) {
                sb.append('+');
                for (int j = 0; j < WIDTH_TRUCK; j++) {
                    int value = truckArea[i][j];
                    if (value == 0) sb.append(' ');
                    else sb.append(value);
                }
                sb.append('+')
                        .append('\n');
            }
            for (int i = 0; i < WIDTH_TRUCK + 2; i++) {
                sb.append('+');
            }
            sb.append('\n')
                    .append('\n');
        }

        return sb.toString();
    }

}

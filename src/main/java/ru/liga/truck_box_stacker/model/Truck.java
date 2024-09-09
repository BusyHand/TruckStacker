
package ru.liga.truck_box_stacker.model;

import lombok.Data;

import static ru.liga.truck_box_stacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truck_box_stacker.config.GlobalSettings.WIDTH_TRUCK;
/**
 * Represents a truck that can hold boxes.
 * The truck has a two-dimensional area defined by global height and width settings.
 */
@Data
public class Truck {
    private int[][] truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK]; // 2D array representing the truck's area.
}

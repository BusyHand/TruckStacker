
package ru.liga.truckstacker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static ru.liga.truckstacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truckstacker.config.GlobalSettings.WIDTH_TRUCK;

/**
 * Represents a truck that can hold boxes.
 * The truck has a two-dimensional area defined by global height and width settings.
 */
@Data
public class Truck {

    @JsonProperty("truckArea")
    private int[][] truckArea;

    public Truck() {
        this.truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK];
    }

    @JsonIgnore
    public boolean isEmpty() {
        int[] floor = truckArea[0];
        for (int cell : floor) {
            boolean isCellEmpty = cell == 0;
            if (!isCellEmpty) {
                return false;
            }
        }
        return true;
    }

    public void setTruckArea(int[][] truckArea) {
        this.truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK];
        for (int i = 0; i < HEIGHT_TRUCK; i++) {
            for (int j = 0; j < WIDTH_TRUCK; j++) {
                this.truckArea[i][j] = truckArea[HEIGHT_TRUCK - 1 - i][j];
            }
        }
    }


}


package ru.liga.truckstacker.service.stacker.impl.burke.validation;

import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.stacker.impl.burke.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

/**
 * Helper class to check if a box can fit in a given truck area based on
 * its coordinates.
 */
public class CellsValid {

    public boolean canFit(CoordinatesInTruckArea coordinates, HeightStatBox heightStatBox, HeightStatTruck heightStatTruck) {
        int[][] containerArea = heightStatTruck.getTruck().getTruckArea();
        Box box = heightStatBox.getBox();

        if (!areCellsAvailable(coordinates, box, containerArea)) {
            return false;
        }

        int floorSupportCount = calculateFloorSupport(coordinates, box, containerArea);
        return isFloorSupportSufficient(floorSupportCount, box.width());
    }

    private boolean areCellsAvailable(CoordinatesInTruckArea coordinates, Box box, int[][] containerArea) {
        for (int i = coordinates.y(); i - coordinates.y() < box.height(); i++) {
            for (int j = coordinates.x(); j - coordinates.x() < box.width(); j++) {
                if (!isBoxCoordinatesValid(i, j, containerArea) || !isCellEmpty(i, j, containerArea)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBoxCoordinatesValid(int i, int j, int[][] containerArea) {
        return i >= 0 &&
                j >= 0 &&
                i < containerArea.length &&
                j < containerArea[0].length;
    }

    private boolean isCellEmpty(int i, int j, int[][] containerArea) {
        return containerArea[i][j] == 0;
    }


    private int calculateFloorSupport(CoordinatesInTruckArea coordinates, Box box, int[][] containerArea) {
        int floorSupportCount = box.width();
        if (isSupportedByFloor(coordinates)) {
            floorSupportCount = 0;
            int rowBelow = coordinates.y() - 1;

            for (int j = coordinates.x(); j - coordinates.x() < box.width(); j++) {
                if (hasSupportAt(j, containerArea[rowBelow])) {
                    floorSupportCount++;
                }
            }
        }
        return floorSupportCount;
    }

    private boolean hasSupportAt(int j, int[] containerArea) {
        return containerArea[j] != 0;
    }

    private boolean isSupportedByFloor(CoordinatesInTruckArea coordinates) {
        return coordinates.y() - 1 >= 0;
    }

    /**
     * Check if the floor support is more than half of the box width
     */
    private boolean isFloorSupportSufficient(int floorSupportCount, int boxWidth) {
        return floorSupportCount > boxWidth / 2.0;
    }
}

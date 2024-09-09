
package ru.liga.truck_box_stacker.service.impl.burke;

import ru.liga.truck_box_stacker.model.Box;
/**
 * Helper class to check if a box can fit in a given truck area based on
 * its coordinates.
 */
class CellsValid {

    public static boolean canFit(CoordinatesInTruckArea coordinates, HeightStatBox heightStatBox, HeightStatTruck heightStatTruck) {
        int[][] containerArea = heightStatTruck.getTruck().getTruckArea();
        Box box = heightStatBox.getBox();
        if (checkAvailableCells(coordinates, box, containerArea)) {
            return false;
        } else {
            int countFloorSupport = getCountFloorSupport(coordinates, box, containerArea);
            return countFloorSupport > box.getWidth() * 1.0 / 2;
        }
    }

    private static boolean checkAvailableCells(CoordinatesInTruckArea coordinates, Box box, int[][] containerArea) {
        if (coordinates.y() >= containerArea.length) {
            return true;
        } else {
            for(int i = coordinates.y(); i - coordinates.y() < box.getHeight(); i++) {
                for(int j = coordinates.x(); j - coordinates.x() < box.getWidth(); j++) {
                    if (i >= containerArea.length || j >= containerArea[0].length || containerArea[i][j] != 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private static int getCountFloorSupport(CoordinatesInTruckArea coordinates, Box box, int[][] containerArea) {
        int countFloorSupport = box.getWidth();
        if (coordinates.y() - 1 >= 0) {
            countFloorSupport = 0;
            int i = coordinates.y() - 1;

            for(int j = coordinates.x(); j - coordinates.x() < box.getWidth(); ++j) {
                if (containerArea[i][j] != 0) {
                    ++countFloorSupport;
                }
            }
        }

        return countFloorSupport;
    }
}

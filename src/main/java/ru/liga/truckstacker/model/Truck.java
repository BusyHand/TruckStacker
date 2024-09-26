
package ru.liga.truckstacker.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import ru.liga.truckstacker.convertor.impl.TruckAreaToBoxesMergeMapConverter;

import java.util.HashMap;
import java.util.Map;

import static ru.liga.truckstacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truckstacker.config.GlobalSettings.WIDTH_TRUCK;

/**
 * Класс, представляющий грузовик, который может вмещать коробки.
 * Грузовик имеет двумерную область, определяемую глобальными значениями высоты и ширины.
 * Класс управляет областью грузовика и методами, связанными с укладкой коробок.
 */
@Data
public class Truck {

    @JsonProperty("truckArea")
    protected int[][] truckArea;

    @JsonIgnore
    private Map<Box, Integer> boxesInTruck;

    public Truck() {
        this.truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK];
        boxesInTruck = new HashMap<>();
    }

    @JsonSetter("truckArea")
    public void setTruckArea(int[][] truckArea) {
        this.truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK];
        for (int i = 0; i < HEIGHT_TRUCK; i++) {
            for (int j = 0; j < WIDTH_TRUCK; j++) {
                this.truckArea[i][j] = truckArea[HEIGHT_TRUCK - 1 - i][j];
            }
        }
        boxesInTruck = TruckAreaToBoxesMergeMapConverter.convert(truckArea);
    }

    @JsonGetter("truckArea")
    public int[][] getTruckArea() {
        int[][] invertedTruckArea = new int[truckArea.length][truckArea[0].length];
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                invertedTruckArea[i][j] = truckArea[truckArea.length - 1 - i][j];
            }
        }
        return invertedTruckArea;
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

    /**
     * Добавляет коробку в грузовик по указанным координатам (x, y).
     * @param x Координата по оси X.
     * @param y Координата по оси Y.
     * @param box Объект коробки, который нужно добавить.
     */
    public void addBoxByCoordinates(int x, int y, Box box) {
        addBoxByCoordinates(new CoordinatesInTruckArea(x, y), box);
    }
    /**
     * Добавляет коробку в грузовик по указанным координатам.
     * @param coordinates Координаты в грузовике, куда будет добавлена коробка.
     * @param box Объект коробки, который нужно добавить.
     */
    public void addBoxByCoordinates(CoordinatesInTruckArea coordinates, Box box) {
        int[][] boxPattern = box.boxPattern();
        int boxHeight = box.height();
        int boxWidth = box.width();

        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (boxPattern[i][j] != 0) {
                    truckArea[coordinates.y() + i][coordinates.x() + j] = boxPattern[i][j];
                }
            }
        }
        boxesInTruck.merge(box, 1, Integer::sum);
    }


    /**
     * Проверяет, может ли коробка вписаться в грузовик по указанным координатам (x, y).
     * @param x Координата по оси X, где начинается добавление коробки.
     * @param y Координата по оси Y, где начинается добавление коробки.
     * @param box Объект коробки, который нужно проверить на возможность добавления.
     * @return true, если коробка может вписаться; иначе false.
     */
    public boolean canFit(int x, int y, Box box) {
        return canFit(new CoordinatesInTruckArea(x, y), box);
    }

    /**
     * Проверяет, может ли коробка вписаться в грузовик по указанным координатам.
     * @param coordinates Координаты в грузовике, откуда начинается проверка.
     * @param box Объект коробки, который нужно проверить на возможность добавления.
     * @return true, если коробка может вписаться; иначе false.
     */
    public boolean canFit(CoordinatesInTruckArea coordinates, Box box) {
        if (!areCellsAvailable(coordinates, box)) {
            return false;
        }

        int floorSupportCount = calculateFloorSupport(coordinates, box);
        return isFloorSupportSufficient(floorSupportCount, box.width());
    }

    private boolean areCellsAvailable(CoordinatesInTruckArea coordinates, Box box) {
        for (int i = coordinates.y(); i - coordinates.y() < box.height(); i++) {
            for (int j = coordinates.x(); j - coordinates.x() < box.width(); j++) {
                if (!isBoxCoordinatesValid(i, j) || !isCellEmpty(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isBoxCoordinatesValid(int i, int j) {
        return i >= 0 &&
                j >= 0 &&
                i < truckArea.length &&
                j < truckArea[0].length;
    }

    private boolean isCellEmpty(int i, int j) {
        return truckArea[i][j] == 0;
    }


    private int calculateFloorSupport(CoordinatesInTruckArea coordinates, Box box) {
        int floorSupportCount = box.width();
        if (isSupportedByFloor(coordinates)) {
            floorSupportCount = 0;
            int rowBelow = coordinates.y() - 1;

            for (int j = coordinates.x(); j - coordinates.x() < box.width(); j++) {
                if (hasSupportAt(j, truckArea[rowBelow])) {
                    floorSupportCount++;
                }
            }
        }
        return floorSupportCount;
    }

    private boolean hasSupportAt(int j, int[] truckArea) {
        return truckArea[j] != 0;
    }

    private boolean isSupportedByFloor(CoordinatesInTruckArea coordinatesInTruckArea) {
        return coordinatesInTruckArea.y() - 1 >= 0;
    }


    private boolean isFloorSupportSufficient(int floorSupportCount, int boxWidth) {
        return floorSupportCount > boxWidth / 2.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = HEIGHT_TRUCK - 1; i > -1; i--) {
            sb.append('+');

            for (int j = 0; j < WIDTH_TRUCK; j++) {
                int value = truckArea[i][j];
                if (value == 0) {
                    sb.append(' ');
                } else {
                    sb.append(value);
                }
            }

            sb.append('+')
                    .append('\n');
        }

        for (int i = 0; i < WIDTH_TRUCK + 2; i++) {
            sb.append('+');
        }
        sb.append('\n')
                .append('\n');

        for (var boxIntegerEntry : boxesInTruck.entrySet()) {
            sb.append(boxIntegerEntry.getKey())
                    .append(" - ")
                    .append(boxIntegerEntry.getValue())
                    .append('\n')
                    .append('\n');
        }

        return sb.toString();
    }
}

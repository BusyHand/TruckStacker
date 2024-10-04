
package ru.liga.truckstacker.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.liga.truckstacker.util.CoordinatesInTruckArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.liga.truckstacker.util.GlobalSettings.*;

/**
 * Класс, представляющий грузовик, который может вмещать коробки.
 * Грузовик имеет двумерную область, определяемую глобальными значениями высоты и ширины.
 * Класс управляет областью грузовика и методами, связанными с укладкой коробок.
 */
@Getter
@EqualsAndHashCode
public class Truck {

    @JsonProperty("truckArea")
    private char[][] truckArea;

    private final int height;
    private final int width;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Setter
    private Map<Box, Integer> boxesInTruck;

    public Truck() {
        this(DEFAULT_HEIGHT_TRUCK, DEFAULT_WIDTH_TRUCK);
    }

    public Truck(int height, int width) {
        boxesInTruck = new HashMap<>();
        this.height = height;
        this.width = width;
        this.truckArea = new char[height][width];
        Arrays.stream(truckArea)
                .forEach(t -> Arrays.fill(t, ' '));
    }

    @JsonSetter("truckArea")
    public void setTruckArea(char[][] truckArea) {
        this.truckArea = new char[truckArea.length][truckArea[0].length];
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                this.truckArea[i][j] = truckArea[truckArea.length - 1 - i][j];
            }
        }
    }

    @JsonGetter("truckArea")
    public char[][] getTruckArea() {
        char[][] invertedTruckArea = new char[truckArea.length][truckArea[0].length];
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                invertedTruckArea[i][j] = truckArea[truckArea.length - 1 - i][j];
            }
        }
        return invertedTruckArea;
    }

    @JsonIgnore
    public boolean isEmpty() {
        char[] floor = truckArea[0];
        for (int cell : floor) {
            if (cell != EMPTY_CELL) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет коробку в грузовик по указанным координатам (x, y).
     *
     * @param x   Координата по оси X.
     * @param y   Координата по оси Y.
     * @param box Объект коробки, который нужно добавить.
     */
    public void addBoxByCoordinates(int x, int y, Box box) {
        addBoxByCoordinates(new CoordinatesInTruckArea(x, y), box);
    }

    /**
     * Добавляет коробку в грузовик по указанным координатам.
     *
     * @param coordinates Координаты в грузовике, куда будет добавлена коробка.
     * @param box         Объект коробки, который нужно добавить.
     */
    public void addBoxByCoordinates(CoordinatesInTruckArea coordinates, Box box) {
        char[][] boxPattern = box.getBoxPattern();
        int boxHeight = box.getHeight();
        int boxWidth = box.getWidth();

        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (boxPattern[i][j] != EMPTY_CELL) {
                    truckArea[coordinates.y() + i][coordinates.x() + j] = boxPattern[i][j];
                }
            }
        }
        boxesInTruck.merge(box, 1, Integer::sum);
    }


    /**
     * Проверяет, может ли коробка вписаться в грузовик по указанным координатам (x, y).
     *
     * @param x   Координата по оси X, где начинается добавление коробки.
     * @param y   Координата по оси Y, где начинается добавление коробки.
     * @param box Объект коробки, который нужно проверить на возможность добавления.
     * @return true, если коробка может вписаться; иначе false.
     */
    public boolean canFit(int x, int y, Box box) {
        return canFit(new CoordinatesInTruckArea(x, y), box);
    }

    /**
     * Проверяет, может ли коробка вписаться в грузовик по указанным координатам.
     *
     * @param coordinates Координаты в грузовике, откуда начинается проверка.
     * @param box         Объект коробки, который нужно проверить на возможность добавления.
     * @return true, если коробка может вписаться; иначе false.
     */
    public boolean canFit(CoordinatesInTruckArea coordinates, Box box) {
        return areCellsAvailable(coordinates, box);
    }

    private boolean areCellsAvailable(CoordinatesInTruckArea coordinates, Box box) {
        int height = box.getHeight();
        int width = box.getWidth();
        for (int i = coordinates.y(); i - coordinates.y() < height; i++) {
            for (int j = coordinates.x(); j - coordinates.x() < width; j++) {
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
        return truckArea[i][j] == EMPTY_CELL;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = truckArea.length - 1; i > -1; i--) {
            sb.append('+');

            for (int j = 0; j < truckArea[0].length; j++) {
                sb.append(truckArea[i][j]);
            }

            sb.append('+')
                    .append('\n');
        }

        for (int i = 0; i < truckArea[0].length + 2; i++) {
            sb.append('+');
        }
        sb.append('\n')
                .append('\n');

        for (var boxIntegerEntry : boxesInTruck.entrySet()) {
            sb.append(boxIntegerEntry.getKey())
                    .append("count in truck = ")
                    .append(boxIntegerEntry.getValue())
                    .append('\n')
                    .append('\n');
        }

        return sb.toString();
    }
}

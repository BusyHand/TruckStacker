
package ru.liga.truckstacker.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.liga.truckstacker.util.CoordinatesInTruckArea;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ru.liga.truckstacker.util.GlobalSettings.*;

/**
 * Класс, представляющий грузовик, который может вмещать коробки.
 * Грузовик имеет двумерную область, определяемую глобальными значениями высоты и ширины.
 * Класс управляет областью грузовика и методами, связанными с укладкой коробок.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
)
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "string-array")
    @Column(
            name = "truck_area",
            columnDefinition = "TEXT[]"
    )
    private String[] truck_area;
    @Transient
    private char[][] truckArea;
    @EqualsAndHashCode.Exclude
    @Transient
    private Map<Box, Integer> boxesInTruck;
    @Transient
    private final int height;
    @Transient
    private final int width;

    public Truck() {
        this(DEFAULT_HEIGHT_TRUCK, DEFAULT_WIDTH_TRUCK);
    }

    public Truck(int height, int width) {
        this(new char[height][width]);
    }

    public Truck(char[][] truckArea) {
        boxesInTruck = new HashMap<>();
        this.height = truckArea.length;
        this.width = truckArea[0].length;
        this.truckArea = truckArea;
        Arrays.stream(truckArea)
                .forEach(t -> Arrays.fill(t, ' '));
    }


    @PrePersist
    public void prePersist() {
        if (truckArea != null) {
            int rows = truckArea.length;
            truck_area = new String[rows];
            for (int i = 0; i < rows; i++) {
                truck_area[i] = new String(truckArea[i]);
            }
        }
    }

    @PostLoad
    public void postLoad() {
        if (truckArea != null) {
            int rows = truckArea.length;
            truckArea = new char[rows][];
            for (int i = 0; i < rows; i++) {
                truckArea[i] = truck_area[i].toCharArray();
            }
        }
    }

    public void setTruckArea(char[][] truckArea) {
        this.truckArea = new char[truckArea.length][truckArea[0].length];
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                this.truckArea[i][j] = truckArea[truckArea.length - 1 - i][j];
            }
        }
    }

    public char[][] getTruckArea() {
        char[][] invertedTruckArea = new char[truckArea.length][truckArea[0].length];
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                invertedTruckArea[i][j] = truckArea[truckArea.length - 1 - i][j];
            }
        }
        return invertedTruckArea;
    }


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

}

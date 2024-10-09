package ru.liga.truckstacker.service.stacker.impl.burke.model;

import lombok.Getter;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.util.CoordinatesInTruckArea;

import java.util.Arrays;

/**
 * Класс, представляющий статистику высоты грузовика.
 * Этот класс хранит информацию о высоте каждого столбца в грузовике.
 */
@Getter
public class HeightStatTruck {
    private final Truck truck;
    private int[] heightStat;

    public HeightStatTruck() {
        this(new Truck());
    }

    public HeightStatTruck(Truck truck) {
        this.truck = truck;
        heightStat = new int[truck.getWidth()];
    }

    /**
     * Находит координаты с минимальной высотой.
     * <p>
     * Этот метод проходит по массиву высот и находит индекс
     * (координату) элемента с наименьшим значением высоты.
     *
     * @return Возвращает объект CoordinatesInTruckArea, содержащий
     * координаты (индекс) элемента с минимальной высотой
     * и его значение высоты.
     */
    public CoordinatesInTruckArea findMinHeightCoordinates() {
        int x = 0;
        int y = Integer.MAX_VALUE;

        for (int i = 0; i < heightStat.length; i++) {
            if (y > heightStat[i]) {
                x = i;
                y = heightStat[i];
            }
        }
        return new CoordinatesInTruckArea(x, y);
    }

    /**
     * Проверяет, заполнен ли грузовик на максимальную высоту.
     *
     * @return true, если грузовик заполнен; иначе false.
     */
    public boolean isFull() {
        int sum = Arrays.stream(heightStat).sum();
        return sum == truck.getHeight() * truck.getWidth();
    }

    /**
     * Проверяет, пустой ли грузовик.
     *
     * @return true, если грузовик не пуст; иначе false.
     */
    public boolean isNotEmpty() {
        int sum = Arrays.stream(heightStat).sum();
        return sum != 0;
    }

    /**
     * Увеличивает высоту в грузовике там, где была уложена коробка.
     *
     * @param coordinates Координаты, по которым была уложена коробка.
     * @param box         Коробка, высота которой добавляется.
     */
    public void raiseHeightWhereBoxStacked(CoordinatesInTruckArea coordinates, HeightStatBox box) {
        int[] heightStatBox = box.getHeightStat();
        int i = coordinates.x();

        for (int j = 0; j < heightStatBox.length; j++, i++) {
            heightStat[i] += heightStatBox[j];
        }
    }

    /**
     * Увеличивает высоту в грузовике до минимальной высоты среди соседних ячеек.
     *
     * @param coordinates Координаты, для которых нужно установить высоту.
     */
    public void raiseHeightToLowestNeighbour(CoordinatesInTruckArea coordinates) {
        int rightLimit = findRightLimitForUniformHeight(coordinates);
        int minValue = findMinHeightFromAdjacentCells(coordinates, rightLimit);

        for (int i = coordinates.x(); i <= rightLimit; i++) {
            heightStat[i] = minValue;
        }
    }

    private int findRightLimitForUniformHeight(CoordinatesInTruckArea coordinates) {
        int rightBound = coordinates.x();

        for (int i = coordinates.x() + 1; i < heightStat.length; i++) {
            int currentHeight = heightStat[i];
            int previousHeight = heightStat[i - 1];
            if (currentHeight == previousHeight) {
                rightBound = i;
            }
        }

        return rightBound;
    }

    private int findMinHeightFromAdjacentCells(CoordinatesInTruckArea coordinates, int rightLimit) {
        int leftNeighbourHeight = getLeftNeighbourHeight(coordinates);
        int rightNeighbourHeight = getRightNeighbourHeight(rightLimit);
        int minHeight;

        if (isLeftAndRightMaxHeight(leftNeighbourHeight, rightNeighbourHeight)) {
            minHeight = getFirstHeightPlusOne(heightStat);
        } else {
            minHeight = Math.min(leftNeighbourHeight, rightNeighbourHeight);
        }

        return minHeight;
    }

    private int getRightNeighbourHeight(int rightLimit) {
        return isAtRightBoundary(rightLimit) ? Integer.MAX_VALUE : heightStat[getNextRightIndex(rightLimit)];
    }

    private int getLeftNeighbourHeight(CoordinatesInTruckArea coordinates) {
        return isAtLeftBoundary(coordinates) ? Integer.MAX_VALUE : heightStat[getPreviousLeftIndex(coordinates)];
    }

    private boolean isAtRightBoundary(int rightLimit) {
        return rightLimit >= heightStat.length - 1;
    }

    private boolean isAtLeftBoundary(CoordinatesInTruckArea coordinates) {
        return coordinates.x() <= 0;
    }

    private int getNextRightIndex(int rightLimit) {
        return rightLimit + 1;
    }

    private int getPreviousLeftIndex(CoordinatesInTruckArea coordinates) {
        return coordinates.x() - 1;
    }

    private boolean isLeftAndRightMaxHeight(int leftHeight, int rightHeight) {
        return leftHeight == Integer.MAX_VALUE && rightHeight == Integer.MAX_VALUE;
    }

    private int getFirstHeightPlusOne(int[] heightStat) {
        return heightStat[0] + 1;
    }
}

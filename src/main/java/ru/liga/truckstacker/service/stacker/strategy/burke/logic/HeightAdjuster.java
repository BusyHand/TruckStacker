package ru.liga.truckstacker.service.stacker.strategy.burke.logic;

import ru.liga.truckstacker.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatTruck;
/**
 * Класс для управления высотой укладки коробок в грузовиках.
 * Этот класс отвечает за изменение высоты в грузовике в зависимости от
 * расположения коробок. Он обеспечивает корректное обновление высоты ячеек
 * грузовика при укладке новых коробок и контролирует координаты верхней
 * границы уже уложенных коробок.
 */
public class HeightAdjuster {

    /**
     * Увеличивает высоту в грузовике там, где была уложена коробка.
     * @param coordinates Координаты, по которым была уложена коробка.
     * @param box Коробка, высота которой добавляется.
     * @param heightStatTruck Грузовик, в который была уложена коробка.
     */
    public void raiseHeightWhereBoxStacked(CoordinatesInTruckArea coordinates, HeightStatBox box, HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int[] heightStatBox = box.getHeightStat();
        int i = coordinates.x();

        for (int j = 0; j < heightStatBox.length; j++, i++) {
            heightStat[i] += heightStatBox[j];
        }
    }
    /**
     * Увеличивает высоту в грузовике до минимальной высоты среди соседних ячеек.
     * @param coordinates Координаты, для которых нужно установить высоту.
     * @param heightStatTruck Грузовик, высота которого будет обновлена.
     */
    public void raiseHeightToLowestNeighbour(CoordinatesInTruckArea coordinates, HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int rightLimit = findRightLimitForUniformHeight(coordinates, heightStat);
        int minValue = findMinHeightFromAdjacentCells(coordinates, heightStat, rightLimit);

        for (int i = coordinates.x(); i <= rightLimit; i++) {
            heightStat[i] = minValue;
        }
    }

    private int findRightLimitForUniformHeight(CoordinatesInTruckArea coordinates, int[] heightStat) {
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

    private int findMinHeightFromAdjacentCells(CoordinatesInTruckArea coordinates, int[] heightStat, int rightLimit) {
        int leftNeighbourHeight = getLeftNeighbourHeight(coordinates, heightStat);
        int rightNeighbourHeight = getRightNeighbourHeight(heightStat, rightLimit);
        int minHeight;

        if (isLeftAndRightMaxHeight(leftNeighbourHeight, rightNeighbourHeight)) {
            minHeight = getFirstHeightPlusOne(heightStat);
        } else {
            minHeight = Math.min(leftNeighbourHeight, rightNeighbourHeight);
        }

        return minHeight;
    }

    private int getRightNeighbourHeight(int[] heightStat, int rightLimit) {
        return isAtRightBoundary(rightLimit, heightStat) ? Integer.MAX_VALUE : heightStat[getNextRightIndex(rightLimit)];
    }

    private int getLeftNeighbourHeight(CoordinatesInTruckArea coordinates, int[] heightStat) {
        return isAtLeftBoundary(coordinates) ? Integer.MAX_VALUE : heightStat[getPreviousLeftIndex(coordinates)];
    }

    private boolean isAtRightBoundary(int rightLimit, int[] heightStat) {
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

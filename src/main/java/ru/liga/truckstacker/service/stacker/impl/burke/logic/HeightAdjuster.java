package ru.liga.truckstacker.service.stacker.impl.burke.logic;

import ru.liga.truckstacker.service.stacker.impl.burke.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

public class HeightAdjuster {

    public void raiseHeightWhereBoxStacked(CoordinatesInTruckArea coordinates, HeightStatBox box, HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int[] heightStatBox = box.getHeightStat();
        int i = coordinates.x();

        for (int j = 0; j < heightStatBox.length; j++, i++) {
            heightStat[i] += heightStatBox[j];
        }
    }

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

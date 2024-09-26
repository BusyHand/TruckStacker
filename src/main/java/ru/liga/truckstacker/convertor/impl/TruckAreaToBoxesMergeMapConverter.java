package ru.liga.truckstacker.convertor.impl;

import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.BoxType;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TruckAreaToBoxesMergeMapConverter {

    public static Map<Box, Integer> convert(int[][] truckArea) {
        return parseTruckArea(truckArea);
    }

    private static Map<Box, Integer> parseTruckArea(int[][] truckArea) {
        boolean[][] visited = new boolean[truckArea.length][truckArea[0].length];
        Map<Box, Integer> boxIntegerHashMap = new HashMap<>();

        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                int boxValue = truckArea[i][j];
                if (boxValue != 0 && !visited[i][j]) {
                    BoxType boxType = getBoxTypeByValue(boxValue);
                    if (boxType != null) {
                        Box box = boxType.getBox();
                        boxIntegerHashMap.merge(box, 1, Integer::sum);
                        markBoxAsVisited(i, j, boxType, truckArea);
                    }
                }
            }
        }
        return boxIntegerHashMap;
    }

    private static void markBoxAsVisited(int startRow, int startCol, BoxType boxType, int[][] truckArea) {
        for (int row = startRow; row < startRow + boxType.getBox().height(); row++) {
            for (int col = startCol; col < startCol + boxType.getBox().width(); col++) {
                if (row < truckArea.length && col < truckArea[0].length) {
                    truckArea[row][col] = 0;
                }
            }
        }
    }

    private static BoxType getBoxTypeByValue(int value) {
        for (BoxType boxType : BoxType.values()) {
            if (boxType.getBox().boxPattern()[0][0] == value) {
                return boxType;
            }
        }
        return null;
    }
}

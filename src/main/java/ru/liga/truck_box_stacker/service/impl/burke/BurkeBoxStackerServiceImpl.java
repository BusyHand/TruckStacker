
package ru.liga.truck_box_stacker.service.impl.burke;

import org.springframework.stereotype.Component;
import ru.liga.truck_box_stacker.config.bpp.annotation.UseStackerWhen;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.TruckList;
import ru.liga.truck_box_stacker.service.BoxStackerService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static ru.liga.truck_box_stacker.model.TypeAlgorithm.NOT_A_LOT_OF_DATA;

/**
 * Implementation of the BoxStackerService using the Burke algorithm.
 * This service efficiently stacks boxes into trucks based on their
 * dimensions and patterns, optimizing for height.
 * <p>
 * The algorithm operates with a time complexity of O(n²),
 * where n is the number of boxes. This is due to the nested
 * iteration over the boxes for fitting into the truck.
 */
@Component
@UseStackerWhen(NOT_A_LOT_OF_DATA)
public class BurkeBoxStackerServiceImpl implements BoxStackerService {
    /**
     * Stacks a list of boxes into trucks and returns a TruckList.
     * <p>
     * This method processes the provided list of Box objects,
     * converts them into HeightStatBox objects, sorts them,
     * and then applies the Burke stacking algorithm to create a
     * list of HeightStatTruck objects.
     *
     * @param boxList List of Box objects to be stacked into trucks.
     * @return TruckList containing the trucks that successfully
     * stacked the provided boxes.
     */
    @Override
    public TruckList stackBoxes(List<Box> boxList) {
        List<HeightStatBox> heightStatBoxes = boxList.stream()
                .map(HeightStatBox::new)
                .sorted()
                .collect(toCollection(ArrayList::new));

        List<HeightStatTruck> truckList = burkeAlgorithm(heightStatBoxes);

        return new TruckList(truckList.stream()
                .map(HeightStatTruck::getTruck)
                .toList());
    }

    /**
     * Executes the Burke algorithm to stack boxes based on their
     * height statistics.
     * <p>
     * This private method iteratively fits boxes into HeightStatTruck
     * instances until all boxes are processed or no more fitting can
     * occur. It manages the height statistics to ensure optimal stacking.
     *
     * @param heightStatBoxes List of HeightStatBox objects to be
     *                        fitted into trucks.
     * @return List of HeightStatTruck objects representing the
     * final stacked trucks.
     */
    private List<HeightStatTruck> burkeAlgorithm(List<HeightStatBox> heightStatBoxes) {
        List<HeightStatTruck> truckList = new ArrayList<>();
        HeightStatTruck truck = new HeightStatTruck();
        truckList.add(truck);

        while (!heightStatBoxes.isEmpty()) {
            truck = getNewHeightStatTruckIfFull(truckList, truck);
            CoordinatesInTruckArea coordinates = getMinHeightCoordinates(truck);
            boolean boxWasPut = false;
            int indexToDelete = 0;

            for (int i = 0; i < heightStatBoxes.size(); i++) {
                HeightStatBox box = heightStatBoxes.get(i);
                if (CellsValid.canFit(coordinates, box, truck)) {
                    fit(coordinates, box, truck);
                    boxWasPut = true;
                    indexToDelete = i;
                    break;
                }
            }

            if (!boxWasPut) {
                raiseHeightToLowestNeighbour(coordinates, truck);
            } else {
                heightStatBoxes.remove(indexToDelete);
            }
        }

        return truckList;
    }

    private HeightStatTruck getNewHeightStatTruckIfFull(List<HeightStatTruck> truckList, HeightStatTruck truck) {
        if (truck.isFull()) {
            truck = new HeightStatTruck();
            truckList.add(truck);
        }

        return truck;
    }
    /**
     * Determines the coordinates in the truck area that have the minimum
     * height, helping to optimize the stacking process.
     *
     * @param heightStatTruck The truck being evaluated.
     * @return CoordinatesInTruckArea object with the coordinates of
     *         the minimum height.
     */
    private CoordinatesInTruckArea getMinHeightCoordinates(HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int x = 0;
        int y = Integer.MAX_VALUE;

        for (int i = 0; i < heightStat.length; ++i) {
            if (y > heightStat[i]) {
                x = i;
                y = heightStat[i];
            }
        }

        return new CoordinatesInTruckArea(x, y);
    }

    private void fit(CoordinatesInTruckArea coordinates, HeightStatBox heightStatBox, HeightStatTruck heightStatTruck) {
        int[][] containerArea = heightStatTruck.getTruck().getTruckArea();
        Box box = heightStatBox.getBox();
        int[][] patternOfFigure = box.getBoxPattern();
        int i = coordinates.y();

        for (int ki = 0; i - coordinates.y() < box.getHeight() && i < containerArea.length; i++, ki++) {
            int j = coordinates.x();

            for (int kj = 0; j - coordinates.x() < box.getWidth() && j < containerArea[0].length; j++, kj++) {
                containerArea[i][j] = patternOfFigure[ki][kj];
            }

        }

        raiseGap(coordinates, heightStatBox, heightStatTruck);
    }

    private void raiseGap(CoordinatesInTruckArea coordinates, HeightStatBox box, HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int[] heightStatBox = box.getHeightStat();
        int i = coordinates.x();

        for (int j = 0; j < heightStatBox.length; j++, i++) {
            heightStat[i] += heightStatBox[j];
        }

    }

    private void raiseHeightToLowestNeighbour(CoordinatesInTruckArea coordinates, HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int rightBound = getRightBound(coordinates, heightStat);
        int minValue = getMinValueFromNeighbour(coordinates, heightStat, rightBound);

        for (int i = coordinates.x(); i <= rightBound; i++) {
            heightStat[i] = minValue;
        }

    }

    private int getRightBound(CoordinatesInTruckArea coordinates, int[] heightStat) {
        int rightBound = coordinates.x();

        for (int i = coordinates.x() + 1; i < heightStat.length; i++) {
            if (heightStat[i] == heightStat[i - 1]) {
                rightBound = i;
            }
        }

        return rightBound;
    }

    private int getMinValueFromNeighbour(CoordinatesInTruckArea coordinates, int[] heightStat, int rightBound) {
        int leftHeight = coordinates.x() - 1 < 0 ? Integer.MAX_VALUE : heightStat[coordinates.x() - 1];
        int rightHeight = rightBound + 1 >= heightStat.length ? Integer.MAX_VALUE : heightStat[rightBound + 1];
        int minValue;
        if (leftHeight == Integer.MAX_VALUE && rightHeight == Integer.MAX_VALUE) {
            minValue = heightStat[0] + 1;
        } else {
            minValue = Math.min(leftHeight, rightHeight);
        }

        return minValue;
    }
}

package ru.liga.truckstacker.service.stacker.impl.balanced;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerService;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.FreeAreaStatTruck;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static ru.liga.truckstacker.config.TypeAlgorithm.BALANCED;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalancedBoxStackerService implements BoxStackerService {

    private final BoxStackerService dummyBoxStackerServiceImpl;

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return BALANCED;
    }


    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        return dummyBoxStackerServiceImpl.stackBoxes(boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        fillToLimit(truckList, maxTruckNumber);

        PriorityQueue<FreeAreaStatTruck> truckPriorityQueue = getFreeAreaStatTrucks(truckList);
        Deque<AreaStatBox> areaStatBoxDeque = getAreaStatBoxes(boxList);

        log.info("Loaded {} trucks and {} boxes to stack.", truckList.size(), boxList.size());

        List<Truck> result = doBalancedAlgorithm(truckPriorityQueue, areaStatBoxDeque);

        log.info("Finished stacking boxes onto trucks. Total trucks used: {}", result.size());
        return result;
    }

    private List<Truck> doBalancedAlgorithm(PriorityQueue<FreeAreaStatTruck> truckPriorityQueue, Deque<AreaStatBox> areaStatBoxDeque) {
        log.info("Starting balanced stacking algorithm.");

        while (!areaStatBoxDeque.isEmpty()) {
            AreaStatBox box = areaStatBoxDeque.poll();
            FreeAreaStatTruck currentTruck = truckPriorityQueue.poll();

            log.debug("Processing box with area: {}. Current truck free area: {}", box.getArea(), currentTruck.getFreeArea());

            CoordinatesInTruckArea coordinates = findPlaceToFit(currentTruck, box);
            fitTruck(currentTruck, coordinates, box);

            currentTruck.setFreeArea(currentTruck.getFreeArea() - box.getArea());
            log.debug("Box fitted in truck. Updated free area: {}", currentTruck.getFreeArea());

            truckPriorityQueue.add(currentTruck);
        }

        log.info("Balancing algorithm finished. Returning results.");
        return truckPriorityQueue.stream()
                .map(FreeAreaStatTruck::getTruck)
                .toList();
    }


    private void fillToLimit(List<Truck> truckList, int maxTruckNumber) {
        while (truckList.size() < maxTruckNumber) {
            truckList.add(new Truck());
        }
    }

    private PriorityQueue<FreeAreaStatTruck> getFreeAreaStatTrucks(List<Truck> truckList) {
        return truckList.stream()
                .map(FreeAreaStatTruck::new)
                .collect(toCollection(() -> new PriorityQueue<FreeAreaStatTruck>(Comparator.reverseOrder())));
    }

    private Deque<AreaStatBox> getAreaStatBoxes(List<Box> boxList) {
        return boxList.stream()
                .map(AreaStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayDeque::new));
    }


    private CoordinatesInTruckArea findPlaceToFit(FreeAreaStatTruck currentTruck, AreaStatBox box) {
        int[][] truckArea = currentTruck.getTruck().getTruckArea();
        int boxHeight = box.getBox().height();
        int boxWidth = box.getBox().width();

        // Iterate over each possible position in the truck area
        for (int y = 0; y <= truckArea.length - boxHeight; y++) {
            for (int x = 0; x <= truckArea[0].length - boxWidth; x++) {
                // Check if the box can fit at the current coordinates
                if (canBoxFitAt(x, y, box, truckArea)) {
                    return new CoordinatesInTruckArea(x, y);
                }
            }
        }

        // If no valid position is found, return null (or throw an exception depending on the desired behavior)
        throw new RuntimeException("No valid position found to fit the box '" + box.getBox().representation() +
                "' in the largest available truck with area " + currentTruck.getFreeArea() + ".");
    }

    private boolean canBoxFitAt(int x, int y, AreaStatBox box, int[][] truckArea) {
        int[][] boxPattern = box.getBox().boxPattern();
        int boxHeight = box.getBox().height();
        int boxWidth = box.getBox().width();

        // Check if all cells in the truck area at the given coordinates are empty and fit the box pattern
        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (boxPattern[i][j] != 0 && truckArea[y + i][x + j] != 0) {
                    return false;  // The box cannot fit at this position
                }
            }
        }
        int floorSupportCount = calculateFloorSupport(x, y, boxWidth, truckArea);
        return isFloorSupportSufficient(floorSupportCount, boxWidth);
    }

    private void fitTruck(FreeAreaStatTruck currentTruck, CoordinatesInTruckArea coordinates, AreaStatBox box) {
        int[][] truckArea = currentTruck.getTruck().getTruckArea();
        int[][] boxPattern = box.getBox().boxPattern();
        int boxHeight = box.getBox().height();
        int boxWidth = box.getBox().width();

        // Place the box on the truck area at the specified coordinates
        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                if (boxPattern[i][j] != 0) {
                    truckArea[coordinates.y() + i][coordinates.x() + j] = boxPattern[i][j]; // Update truck area
                }
            }
        }
    }

    private int calculateFloorSupport(int x, int y, int width, int[][] containerArea) {
        int floorSupportCount = width;
        if (isSupportedByFloor(y)) {
            floorSupportCount = 0;
            int rowBelow = y - 1;

            for (int j = x; j - x < width; j++) {
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

    private boolean isSupportedByFloor(int y) {
        return y - 1 >= 0;
    }

    /**
     * Check if the floor support is more than half of the box width
     */
    private boolean isFloorSupportSufficient(int floorSupportCount, int boxWidth) {
        return floorSupportCount > boxWidth / 2.0;
    }
}

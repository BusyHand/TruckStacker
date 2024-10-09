package ru.liga.truckstacker.service.stacker.impl.balanced.logic;

import lombok.extern.slf4j.Slf4j;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatTruck;
import ru.liga.truckstacker.util.CoordinatesInTruckArea;

import java.util.Deque;
import java.util.PriorityQueue;

@Slf4j
public class BalancedAlgorithm {

    public PriorityQueue<AreaStatTruck> doBalancedAlgorithm(PriorityQueue<AreaStatTruck> truckPriorityQueue, Deque<AreaStatBox> areaStatBoxDeque) {
        log.info("Starting balanced stacking algorithm.");
        while (!areaStatBoxDeque.isEmpty() && !truckPriorityQueue.isEmpty()) {
            AreaStatBox box = areaStatBoxDeque.poll();
            AreaStatTruck currentTruck = truckPriorityQueue.poll();

            log.debug("Processing box with area: {}. Current truck free area: {}", box.getArea(), currentTruck.getFreeArea());

            CoordinatesInTruckArea coordinates = findPlaceToFit(currentTruck, box);
            currentTruck.getTruck().addBoxByCoordinates(coordinates, box.getBox());

            currentTruck.setFreeArea(currentTruck.getFreeArea() - box.getArea());
            log.debug("Box fitted in truck. Updated free area: {}", currentTruck.getFreeArea());

            truckPriorityQueue.add(currentTruck);
        }

        log.info("Balancing algorithm finished. Returning results.");
        return truckPriorityQueue;
    }

    private CoordinatesInTruckArea findPlaceToFit(AreaStatTruck currentTruck, AreaStatBox areaStatBox) {
        Truck truck = currentTruck.getTruck();
        Box box = areaStatBox.getBox();
        int truckHeight = truck.getHeight();
        int truckWidth = truck.getWidth();
        int boxHeight = box.getHeight();
        int boxWidth = box.getWidth();

        for (int y = 0; y <= truckHeight - boxHeight; y++) {
            for (int x = 0; x <= truckWidth - boxWidth; x++) {
                if (truck.canFit(x, y, box)) {
                    return new CoordinatesInTruckArea(x, y);
                }
            }
        }
        throw new RuntimeException("No valid position found to fit the box '" + box.getForm() +
                "' in the largest available truck with area " + currentTruck.getFreeArea() + ".");
    }
}

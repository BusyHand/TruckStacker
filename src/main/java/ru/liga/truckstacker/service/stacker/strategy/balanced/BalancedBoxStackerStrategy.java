package ru.liga.truckstacker.service.stacker.strategy.balanced;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.service.stacker.strategy.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.strategy.balanced.model.AreaStatTruck;
import ru.liga.truckstacker.service.stacker.strategy.dummy.DummyBoxStackerStrategy;

import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

import static ru.liga.truckstacker.config.TypeAlgorithm.BALANCED;

/**
 * Стратегия укладки коробок с использованием сбалансированного алгоритма.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BalancedBoxStackerStrategy implements BoxStackerStrategy {

    private final DummyBoxStackerStrategy dummyBoxStackerStrategyImpl;
    private final Convertor<List<Box>, Deque<AreaStatBox>> boxesToDequeAreaStatBoxesConvertor;
    private final Convertor<List<Truck>, PriorityQueue<AreaStatTruck>> boxesToPriorityQueueAreaStatTrucksConvertor;
    private final Convertor<PriorityQueue<AreaStatTruck>, List<Truck>> areaStatTrucksToTrucksConvertor;

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return BALANCED;
    }


    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        return dummyBoxStackerStrategyImpl.stackBoxes(boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        fillToLimit(truckList, maxTruckNumber);

        PriorityQueue<AreaStatTruck> truckPriorityQueue = boxesToPriorityQueueAreaStatTrucksConvertor.convert(truckList);
        Deque<AreaStatBox> areaStatBoxDeque = boxesToDequeAreaStatBoxesConvertor.convert(boxList);

        log.info("Loaded {} trucks and {} boxes to stack.", truckList.size(), boxList.size());

        List<Truck> result = doBalancedAlgorithm(truckPriorityQueue, areaStatBoxDeque);

        log.info("Finished stacking boxes onto trucks. Total trucks used: {}", result.size());
        return result;
    }

    private List<Truck> doBalancedAlgorithm(PriorityQueue<AreaStatTruck> truckPriorityQueue, Deque<AreaStatBox> areaStatBoxDeque) {
        log.info("Starting balanced stacking algorithm.");

        while (!areaStatBoxDeque.isEmpty()) {
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
        return areaStatTrucksToTrucksConvertor.convert(truckPriorityQueue);
    }


    private void fillToLimit(List<Truck> truckList, int maxTruckNumber) {
        while (truckList.size() < maxTruckNumber) {
            truckList.add(new Truck());
        }
    }

    private CoordinatesInTruckArea findPlaceToFit(AreaStatTruck currentTruck, AreaStatBox areaStatBox) {
        Truck truck = currentTruck.getTruck();
        Box box = areaStatBox.getBox();
        int[][] truckArea = currentTruck.getTruck().getTruckArea();
        int boxHeight = box.height();
        int boxWidth = box.width();

        for (int y = 0; y <= truckArea.length - boxHeight; y++) {
            for (int x = 0; x <= truckArea[0].length - boxWidth; x++) {
                if (truck.canFit(x, y, box)) {
                    return new CoordinatesInTruckArea(x, y);
                }
            }
        }
        throw new RuntimeException("No valid position found to fit the box '" + box.representation() +
                "' in the largest available truck with area " + currentTruck.getFreeArea() + ".");
    }

}

package ru.liga.truckstacker.service.stacker.impl.dummy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.enums.Algorithm;
import ru.liga.truckstacker.exception.BoxCantStackInTruckException;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;

import java.util.*;

import static java.lang.Math.min;
import static ru.liga.truckstacker.enums.Algorithm.DUMMY;

/**
 * Стратегия укладки коробок с использованием алгоритма Dummy.
 *
 * Этот компонент управляет процессом укладки коробок в грузовики,
 * используя простую стратегию, где каждую коробку помещают в отдельный грузовик.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DummyBoxStackerStrategy implements BoxStackerStrategy {

    @Override
    public Algorithm getAlgorithm() {
        return DUMMY;
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Creating trucks from boxes. Number of boxes: {}", boxList.size());
        List<Truck> trucks = boxList
                .stream()
                .map(this::convertBoxToSingleTruck)
                .toList();
        log.debug("Created {} trucks from boxes.", trucks.size());
        return trucks;
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList, int maxTruckNumber) {
        if (boxList.size() > maxTruckNumber) {
            log.error("The number of boxes ({}) exceeds the available trucks ({}), throwing IllegalArgumentException.", boxList.size(), maxTruckNumber);
            throw new IllegalArgumentException("The number of boxes is greater than the number of trucks, you cannot use a Dummy algorithm.");
        }

        log.info("Stacking boxes into trucks...");
        List<Truck> resultTrucks = stackBoxes(boxList);

        log.info("Stacking completed. Combined truck count: {}", resultTrucks.size());
        return resultTrucks;
    }

    @Override
    public List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        int height = truckExample.getHeight();
        int width = truckExample.getWidth();
        List<Truck> trucks = new ArrayList<>();
        maxTruckNumber = min(boxList.size(), maxTruckNumber);

        for (int i = 0; i < maxTruckNumber; i++) {
            trucks.add(new Truck(height, width));
        }

        return stackBoxes(trucks, boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList) {

        if (boxList.size() > trucks.size()) {
            log.error("The number of boxes ({}) exceeds the available trucks ({}), throwing IllegalArgumentException.", boxList.size(), trucks.size());
            throw new IllegalArgumentException("The number of boxes is greater than the number of trucks, you cannot use a Dummy algorithm.");
        }

        trucks.sort(Comparator.comparing(Truck::getWidth)
                .thenComparing(Truck::getHeight));
        Deque<Truck> truckQueue = new ArrayDeque<>(trucks);

        boxList.sort(Comparator.comparing(Box::getWidth)
                .thenComparing(Box::getHeight));

        List<Truck> resultTrucks = new ArrayList<>();
        while (!truckQueue.isEmpty() && !boxList.isEmpty()) {
            boolean wasAdded = false;
            Truck truck = truckQueue.poll();
            for (int j = 0; j < boxList.size(); j++) {
                Box box = boxList.get(j);
                if (truck.canFit(0, 0, box)) {
                    truck.addBoxByCoordinates(0, 0, box);
                    resultTrucks.add(truck);
                    boxList.remove(j);
                    wasAdded = true;
                    break;
                }
            }
            if (!wasAdded) {
                throw new BoxCantStackInTruckException("Cannot stack box in truck");
            }
        }
        return resultTrucks;
    }

    private Truck convertBoxToSingleTruck(Box box) {
        Truck truck = new Truck();
        if (!truck.canFit(0, 0, box)) {
            throw new IllegalArgumentException("Box cant stack");
        }
        truck.addBoxByCoordinates(0, 0, box);
        return truck;
    }
}

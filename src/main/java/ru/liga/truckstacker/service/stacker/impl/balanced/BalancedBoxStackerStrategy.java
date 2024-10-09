package ru.liga.truckstacker.service.stacker.impl.balanced;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.service.stacker.impl.balanced.logic.BalancedAlgorithm;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatTruck;
import ru.liga.truckstacker.service.stacker.impl.dummy.DummyBoxStackerStrategy;
import ru.liga.truckstacker.util.Algorithm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.Math.min;
import static ru.liga.truckstacker.util.Algorithm.BALANCED;

/**
 * Стратегия укладки коробок с использованием сбалансированного алгоритма.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BalancedBoxStackerStrategy implements BoxStackerStrategy {

    private final DummyBoxStackerStrategy dummyBoxStackerStrategyImpl;

    private final BalancedAlgorithm balancedAlgorithm = new BalancedAlgorithm();

    @Override
    public Algorithm getAlgorithm() {
        return BALANCED;
    }


    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        return dummyBoxStackerStrategyImpl.stackBoxes(boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList, int maxTruckNumber) {
        maxTruckNumber = min(boxList.size(), maxTruckNumber);
        List<Truck> trucks = fillToLimit(maxTruckNumber);
        return stackBoxesCommon(trucks, boxList, trucks.size());
    }


    @Override
    public List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList) {
        return stackBoxesCommon(trucks, boxList, trucks.size());
    }


    @Override
    public List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        maxTruckNumber = min(boxList.size(), maxTruckNumber);
        List<Truck> trucks = fillToLimit(truckExample.getHeight(), truckExample.getWidth(), maxTruckNumber);
        return stackBoxesCommon(trucks, boxList, maxTruckNumber);
    }

    private List<Truck> stackBoxesCommon(List<Truck> trucks, List<Box> boxList, int maxTruckNumber) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);

        PriorityQueue<AreaStatTruck> truckPriorityQueue = Convertor.trucksToPriorityQueueAreaStatTrucks(trucks);
        Deque<AreaStatBox> areaStatBoxDeque = Convertor.boxesToDequeAreaStatBoxes(boxList);

        log.info("Loaded {} trucks and {} boxes to stack.", trucks.size(), boxList.size());

        PriorityQueue<AreaStatTruck> result = balancedAlgorithm.doBalancedAlgorithm(truckPriorityQueue, areaStatBoxDeque);

        log.info("Finished stacking boxes onto trucks. Total trucks used: {}", result.size());
        return Convertor.unwrapAreaStatTrucksConvertor(result);
    }

    private List<Truck> fillToLimit(int maxTruckNumber) {
        Truck truck = new Truck();
        return fillToLimit(truck.getHeight(), truck.getWidth(), maxTruckNumber);
    }

    private List<Truck> fillToLimit(int height, int width, int maxTruckNumber) {
        List<Truck> trucks = new ArrayList<>();
        while (trucks.size() < maxTruckNumber) {
            trucks.add(new Truck(height, width));
        }
        return trucks;
    }

}

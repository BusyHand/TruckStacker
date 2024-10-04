
package ru.liga.truckstacker.service.stacker.impl.burke;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.service.stacker.impl.burke.logic.BurkeAlgorithm;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;
import ru.liga.truckstacker.util.TypeAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;
import static ru.liga.truckstacker.util.TypeAlgorithm.BURKE;

/**
 * Стратегия укладки коробок с использованием алгоритма Бёрка.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BurkeBoxStackerStrategy implements BoxStackerStrategy {


    private final BurkeAlgorithm burkeAlgorithm = new BurkeAlgorithm();

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return BURKE;
    }


    @Override
    public List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        List<Truck> trucks = fillAllTrucksToLimitedList(truckExample, boxList, maxTruckNumber);
        return stackBoxes(trucks, boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList) {
        return stackBoxesCommon(trucks, boxList, trucks.size());
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Starting to stack boxes without truck limitation.");
        return stackBoxesCommon(boxList, Integer.MAX_VALUE);
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList, int maxTruckNumber) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        return stackBoxesCommon(boxList, maxTruckNumber);
    }

    private List<Truck> stackBoxesCommon(List<Box> boxList, int maxTruckNumber) {
        return stackBoxesCommon(Collections.EMPTY_LIST, boxList, maxTruckNumber);
    }

    private List<Truck> stackBoxesCommon(List<Truck> trucks, List<Box> boxList, int maxTruckNumber) {
        log.debug("Input box list size: {}", boxList.size());

        List<HeightStatBox> heightStatBoxes = Convertor.boxesToHeightStatBoxes(boxList);
        log.debug("Converted {} boxes to HeightStatBoxes.", heightStatBoxes.size());
        List<HeightStatTruck> heightStatTrucks = Convertor.wrapHeightStatTrucks(trucks);

        List<HeightStatTruck> stackedTrucks;
        if (!heightStatTrucks.isEmpty()) {
            stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, heightStatBoxes);
        } else if (maxTruckNumber == Integer.MAX_VALUE) {
            stackedTrucks = burkeAlgorithm.stackBox(heightStatBoxes);
        } else {
            stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, maxTruckNumber, heightStatBoxes);
        }

        log.info("Stacking completed. Number of trucks used: {}", stackedTrucks.size());
        return Convertor.unwrapHeightStatTrucksConvertor(stackedTrucks);
    }

    private List<Truck> fillAllTrucksToLimitedList(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        int height = truckExample.getHeight();
        int width = truckExample.getWidth();
        maxTruckNumber = min(boxList.size(), maxTruckNumber);
        List<Truck> trucks = new ArrayList<>();

        for (int i = 0; i < maxTruckNumber; i++) {
            trucks.add(new Truck(height, width));
        }
        return trucks;
    }
}

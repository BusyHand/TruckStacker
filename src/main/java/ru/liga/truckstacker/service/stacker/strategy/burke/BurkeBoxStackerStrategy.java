
package ru.liga.truckstacker.service.stacker.strategy.burke;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.service.stacker.strategy.burke.logic.BurkeAlgorithm;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatTruck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.liga.truckstacker.config.TypeAlgorithm.BURKE;

/**
 * Стратегия укладки коробок с использованием алгоритма Бёрка.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BurkeBoxStackerStrategy implements BoxStackerStrategy {

    private final BurkeAlgorithm burkeAlgorithm = new BurkeAlgorithm();
    private final Convertor<List<Box>, List<HeightStatBox>> boxesHeightStatBoxesConvertor;
    private final Convertor<List<Truck>, List<HeightStatTruck>> trucksHeightStatTrucksConvertor;
    private final Convertor<List<HeightStatTruck>, List<Truck>> unwrappedTrucksConverter;

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return BURKE;
    }


    @Override
    public List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        return stackBoxesCommon(truckList, boxList, maxTruckNumber);
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Starting to stack boxes without truck limitation.");
        return stackBoxesCommon(Collections.emptyList(), boxList, Integer.MAX_VALUE);
    }

    private List<Truck> stackBoxesCommon(List<Truck> truckList, List<Box> boxList, int maxTruckNumber) {
        log.debug("Input box list size: {}", boxList.size());

        List<HeightStatBox> heightStatBoxes = boxesHeightStatBoxesConvertor.convert(boxList);
        log.debug("Converted {} boxes to HeightStatBoxes.", heightStatBoxes.size());

        List<HeightStatTruck> heightStatTrucks = !truckList.isEmpty()
                ? trucksHeightStatTrucksConvertor.convert(truckList)
                : new ArrayList<>();

        List<HeightStatTruck> stackedTrucks;
        if (maxTruckNumber != Integer.MAX_VALUE) {
            log.debug("Input truck list size: {}", heightStatTrucks.size());
            stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, maxTruckNumber, heightStatBoxes);
        } else {
            stackedTrucks = burkeAlgorithm.stackBox(heightStatBoxes);
        }

        log.info("Stacking completed. Number of trucks used: {}", stackedTrucks.size());
        return unwrappedTrucksConverter.convert(stackedTrucks);
    }

}

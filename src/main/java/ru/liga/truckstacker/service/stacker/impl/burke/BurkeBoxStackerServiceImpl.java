
package ru.liga.truckstacker.service.stacker.impl.burke;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerService;
import ru.liga.truckstacker.service.stacker.impl.burke.logic.BurkeAlgorithm;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toCollection;
import static ru.liga.truckstacker.config.TypeAlgorithm.BURKE;

/**
 * Implementation of the BoxStackerService using the Burke algorithm.
 * This service efficiently stacks boxes into trucks based on their
 * dimensions and patterns, optimizing for height.
 * <p>
 * The algorithm operates with a time complexity of O(n²),
 * where n is the number of boxes. This is due to the nested
 * iteration over the boxes for fitting into the truck.
 */
@Service()
@Slf4j
public class BurkeBoxStackerServiceImpl implements BoxStackerService {

    private final BurkeAlgorithm burkeAlgorithm = new BurkeAlgorithm();

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return BURKE;
    }


    @Override
    public List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        log.debug("Input truck list size: {}, Input box list size: {}", truckList.size(), boxList.size());

        List<HeightStatBox> heightStatBoxes = convertToHeightStatBox(boxList);
        log.debug("Converted {} boxes to HeightStatBoxes.", heightStatBoxes.size());

        List<HeightStatTruck> heightStatTrucks = convertToHeightStatTruck(truckList);
        log.debug("Converted {} trucks to HeightStatTrucks.", heightStatTrucks.size());

        List<HeightStatTruck> stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, maxTruckNumber, heightStatBoxes);
        log.info("Stacking completed. Number of trucks used: {}", stackedTrucks.size());

        return convertToTruck(stackedTrucks);
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Starting to stack boxes without truck limitation.");
        log.debug("Input box list size: {}", boxList.size());

        List<HeightStatBox> heightStatBoxes = convertToHeightStatBox(boxList);
        log.debug("Converted {} boxes to HeightStatBoxes.", heightStatBoxes.size());

        List<HeightStatTruck> truckList = burkeAlgorithm.stackBox(heightStatBoxes);
        log.info("Stacking completed. Number of trucks used: {}", truckList.size());

        return convertToTruck(truckList);
    }


    private List<HeightStatBox> convertToHeightStatBox(List<Box> boxList) {
        return boxList.stream()
                .map(HeightStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(toCollection(ArrayList::new));
    }

    private List<HeightStatTruck> convertToHeightStatTruck(List<Truck> truckList) {
        return truckList.stream()
                .map(truck -> new HeightStatTruck(truck))
                .collect(toCollection(ArrayList::new));
    }

    private List<Truck> convertToTruck(List<HeightStatTruck> stackedTrucks) {
        return stackedTrucks.stream()
                .map(HeightStatTruck::getTruck)
                .toList();
    }
}

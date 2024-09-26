package ru.liga.truckstacker.service.stacker.strategy.dummy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;

import java.util.List;
import java.util.stream.Stream;

import static ru.liga.truckstacker.config.TypeAlgorithm.DUMMY;

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

    private final Convertor<Box, Truck> boxSingleTruckConvertor;

    @Override
    public TypeAlgorithm getTypeAlgorithm() {
        return DUMMY;
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList) {
        log.info("Starting stacking process with max truck number: {}", maxTruckNumber);

        List<Truck> notEmptiesTrucks = truckList.stream()
                .filter(truck -> !truck.isEmpty())
                .toList();

        log.debug("Found {} non-empty trucks.", notEmptiesTrucks.size());

        if (boxList.size() > maxTruckNumber - notEmptiesTrucks.size()) {
            log.error("The number of boxes ({}) exceeds the available trucks ({}), throwing IllegalArgumentException.",
                    boxList.size(), maxTruckNumber - notEmptiesTrucks.size());
            throw new IllegalArgumentException("The number of boxes is greater than the number of trucks, you cannot use a Dummy algorithm.");
        }

        log.info("Stacking boxes into trucks...");
        List<Truck> resultTrucks = stackBoxes(boxList);

        log.info("Stacking completed. Combined truck count: {}", notEmptiesTrucks.size() + resultTrucks.size());
        return Stream.concat(notEmptiesTrucks.stream(), resultTrucks.stream()).toList();
    }


    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Creating trucks from boxes. Number of boxes: {}", boxList.size());
        List<Truck> trucks = boxList.stream()
                .map(boxSingleTruckConvertor::convert)
                .toList();

        log.debug("Created {} trucks from boxes.", trucks.size());
        return trucks;
    }
}

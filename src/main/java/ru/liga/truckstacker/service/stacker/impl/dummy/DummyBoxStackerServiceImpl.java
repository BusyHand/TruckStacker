package ru.liga.truckstacker.service.stacker.impl.dummy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.BoxStackerService;

import java.util.List;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static ru.liga.truckstacker.config.TypeAlgorithm.DUMMY;

@Service
@RequiredArgsConstructor
@Slf4j
public class DummyBoxStackerServiceImpl implements BoxStackerService {

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
                .map(this::createTruckFromBox)
                .toList();

        log.debug("Created {} trucks from boxes.", trucks.size());
        return trucks;
    }


    private Truck createTruckFromBox(Box box) {
        Truck truck = new Truck();
        int[][] truckArea = truck.getTruckArea();
        int[][] boxPattern = box.boxPattern();
        for (int i = 0; i < box.height(); i++) {
            if (box.width() >= 0) {
                arraycopy(boxPattern[i], 0, truckArea[i], 0, box.width());
            }
        }
        return truck;
    }
}

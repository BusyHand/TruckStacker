package ru.liga.truckstacker.service.stacker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.config.bpp.TypeAlgorithm;
import ru.liga.truckstacker.config.bpp.annotation.TypeAlgorithmToBeanName;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.repository.TruckRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoxStackerServiceFactory {

    @TypeAlgorithmToBeanName
    private Map<TypeAlgorithm, String> typeAlgorithmToBeanNames;
    private final Map<String, BoxStackerService> beanNamesToAlgorithm;

    private final TruckRepository truckRepository;

    public List<Truck> stackBoxesAndSaveTrucks(List<Truck> trucks, int maxSize, List<Box> boxes, String typeAlgorithmName) {
        log.info("Starting the box stacking process using algorithm: {}", typeAlgorithmName);
        log.debug("Input trucks size: {}, Max size: {}, Number of boxes: {}", trucks.size(), maxSize, boxes.size());

        TypeAlgorithm typeAlgorithm = TypeAlgorithm.getTypeAlgorithmByAlgorithmName(typeAlgorithmName);
        String beanName = typeAlgorithmToBeanNames.get(typeAlgorithm);

        if (beanName == null) {
            log.error("No bean name found for type algorithm: {}", typeAlgorithm);
            throw new IllegalArgumentException("Invalid algorithm type: " + typeAlgorithmName);
        }

        BoxStackerService boxStackerService = beanNamesToAlgorithm.get(beanName);
        if (boxStackerService == null) {
            log.error("No BoxStackerService found for bean name: {}", beanName);
            throw new IllegalArgumentException("No BoxStackerService found for algorithm: " + typeAlgorithmName);
        }

        log.debug("Using BoxStackerService: {}", boxStackerService.getClass().getSimpleName());
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, maxSize, boxes);
        log.info("Box stacking completed. Resulting trucks count: {}", resultTrucks.size());

        List<Truck> savedTrucks = truckRepository.save(resultTrucks);
        log.info("Saved {} trucks to the repository.", savedTrucks.size());
        return savedTrucks;
    }
}

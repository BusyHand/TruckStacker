package ru.liga.truckstacker.service.stacker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.repository.TruckRepository;
import ru.liga.truckstacker.service.stacker.request.BoxStackingRequest;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoxStackerServiceFactory {

    private final Map<TypeAlgorithm, BoxStackerService> typeAlgorithmToBoxStackerService;

    private final TruckRepository truckRepository;

    public List<Truck> stackBoxesAndSaveTrucks(BoxStackingRequest boxStackingRequest) {
        String typeAlgorithmName = boxStackingRequest.getTypeAlgorithmName();
        List<Truck> trucks = boxStackingRequest.getTrucks();
        List<Box> boxes = boxStackingRequest.getBoxes();
        int maxSizeNumberOfTrucks = boxStackingRequest.getMaxSizeNumberOfTrucks();

        log.info("Starting the box stacking process using algorithm: {}", typeAlgorithmName);
        log.debug("Input trucks size: {}, Max size: {}, Number of boxes: {}", trucks.size(), maxSizeNumberOfTrucks, boxes.size());

        TypeAlgorithm typeAlgorithm = TypeAlgorithm.getTypeAlgorithmByAlgorithmName(typeAlgorithmName);
        BoxStackerService boxStackerService = typeAlgorithmToBoxStackerService.get(typeAlgorithm);

        log.debug("Using BoxStackerService: {}", boxStackerService.getClass().getSimpleName());
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, maxSizeNumberOfTrucks, boxes);
        log.info("Box stacking completed. Resulting trucks count: {}", resultTrucks.size());

        List<Truck> savedTrucks = truckRepository.save(resultTrucks);
        log.info("Saved {} trucks to the repository.", savedTrucks.size());
        return savedTrucks;
    }
}

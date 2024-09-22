package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.controller.StackingRequest;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.reader.BoxReaderConvertorService;
import ru.liga.truckstacker.service.reader.TrucksReaderConvertorService;
import ru.liga.truckstacker.service.stacker.BoxStackerServiceFactory;
import ru.liga.truckstacker.service.stacker.request.BoxStackingRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsoleService {

    private final TrucksReaderConvertorService trucksDtoReaderConvertorService;
    private final BoxReaderConvertorService boxReaderConvertorService;
    private final BoxStackerServiceFactory boxStackerServiceFactory;

    public List<Truck> stackBoxesAndSaveTrucks(StackingRequest request) {
        String trucksFileName = request.getTrucksFileName();
        log.debug("Reading and converting trucks from file: {}", trucksFileName);
        List<Truck> trucks = trucksDtoReaderConvertorService.readAndConvertToTruckList(trucksFileName);
        log.debug("Converted {} trucks from file.", trucks.size());

        String boxesFileName = request.getBoxesFileName();
        log.debug("Reading and converting boxes from file: {}", boxesFileName);
        List<Box> boxes = boxReaderConvertorService.readAndConvertToBoxList(boxesFileName);
        log.debug("Converted {} boxes from file.", boxes.size());

        int maxSizeNumberOfTrucks = request.getMaxSizeNumberOfTrucks();
        String typeAlgorithmName = request.getTypeAlgorithmName();

        BoxStackingRequest boxStackingRequest = BoxStackingRequest.builder()
                .trucks(trucks)
                .boxes(boxes)
                .maxSizeNumberOfTrucks(maxSizeNumberOfTrucks)
                .typeAlgorithmName(typeAlgorithmName)
                .build();

        log.info("Starting box stacking and saving trucks process.");
        List<Truck> resultTrucks = boxStackerServiceFactory.stackBoxesAndSaveTrucks(boxStackingRequest);
        log.info("Box stacking and saving completed. Total trucks saved: {}", resultTrucks.size());
        return resultTrucks;
    }
}

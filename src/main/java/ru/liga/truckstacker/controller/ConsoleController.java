package ru.liga.truckstacker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.reader.BoxReaderConvertorService;
import ru.liga.truckstacker.service.reader.TrucksReaderConvertorService;
import ru.liga.truckstacker.service.stacker.BoxStackerServiceFactory;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConsoleController {

    private final TrucksReaderConvertorService trucksDtoReaderConvertorService;
    private final BoxReaderConvertorService boxReaderConvertorService;
    private final BoxStackerServiceFactory boxStackerServiceFactory;

    public List<Truck> request(String trucksFilename, String boxesFilename, int maxNumberTrucks, String typeAlgorithmName) {
        log.info("Request received to stack boxes. Trucks file: {}, Boxes file: {}, Max number of trucks: {}, Algorithm type: {}",
                trucksFilename, boxesFilename, maxNumberTrucks, typeAlgorithmName);

        log.debug("Reading and converting trucks from file: {}", trucksFilename);
        List<Truck> trucks = trucksDtoReaderConvertorService.readAndConvertToTruckList(trucksFilename);
        log.debug("Converted {} trucks from file.", trucks.size());

        log.debug("Reading and converting boxes from file: {}", boxesFilename);
        List<Box> boxes = boxReaderConvertorService.readAndConvertToBoxList(boxesFilename);
        log.debug("Converted {} boxes from file.", boxes.size());

        log.info("Starting box stacking and saving trucks process.");
        List<Truck> resultTrucks = boxStackerServiceFactory.stackBoxesAndSaveTrucks(trucks, maxNumberTrucks, boxes, typeAlgorithmName);

        log.info("Box stacking and saving completed. Total trucks saved: {}", resultTrucks.size());
        return resultTrucks;
    }
}

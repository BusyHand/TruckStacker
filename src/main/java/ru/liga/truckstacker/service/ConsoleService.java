package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.controller.StackingRequest;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.reader.BoxReaderConvertorService;
import ru.liga.truckstacker.service.reader.TrucksReaderConvertorService;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategyService;
import ru.liga.truckstacker.controller.BoxStackingRequest;

import java.util.List;
/**
 * Класс, предоставляющий операции по укладке коробок и сохранению грузовиков.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConsoleService {

    private final TrucksReaderConvertorService trucksDtoReaderConvertorService;
    private final BoxReaderConvertorService boxReaderConvertorService;
    private final BoxStackerStrategyService boxStackerStrategyService;

    /**
     * Укладывает коробки в грузовики и сохраняет их.
     * @param request Запрос на укладку, содержащий информацию о файлах грузовиков и коробок,
     *                максимальном количестве грузовиков и типе алгоритма.
     * @return Список грузовиков, с которыми была выполнена укладка.
     */
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
        List<Truck> resultTrucks = boxStackerStrategyService.stackBoxesAndSaveTrucks(boxStackingRequest);
        log.info("Box stacking and saving completed. Total trucks saved: {}", resultTrucks.size());
        return resultTrucks;
    }
}

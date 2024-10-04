package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.convertor.StackingRequestConvertor;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.repository.truck.TruckRepository;
import ru.liga.truckstacker.service.parser.BoxFileParserService;
import ru.liga.truckstacker.service.parser.TruckJsonFileParserService;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.util.TypeAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс, предоставляющий операции по укладке коробок и сохранению грузовиков.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TruckService {

    private final Map<TypeAlgorithm, BoxStackerStrategy> boxStackerStrategyFactory;

    private final BoxService boxService;

    private final TruckRepository truckRepository;

    private final TruckJsonFileParserService truckJsonFileParserService;

    private final BoxFileParserService boxFileParserService;

    /**
     * Укладывает коробки в грузовики и сохраняет их в репозитории.
     *
     * @param stackingRequest Запрос на укладку, содержащий информацию о файлах грузовиков и коробок,
     *                       максимальном количестве грузовиков и типе алгоритма.
     * @return Список грузовиков, с которыми была выполнена укладка.
     */
    public List<Truck> stackBoxesAndSaveTrucks(StackingRequest stackingRequest) {
        List<Box> boxes = fillAllBoxesFromStackingRequest(stackingRequest);
        List<Truck> trucksWithSize = StackingRequestConvertor.getTrucksBySize(stackingRequest);
        int limitNumberOfTrucks = stackingRequest.getLimitNumberOfTrucks();
        TypeAlgorithm typeAlgorithm = TypeAlgorithm.getTypeAlgorithmByAlgorithmName(stackingRequest.getTypeAlgorithmName());
        BoxStackerStrategy boxStackerStrategy = boxStackerStrategyFactory.get(typeAlgorithm);

        List<Truck> resultTrucks = switch (trucksWithSize.size()) {
            case 0 -> boxStackerStrategy.stackBoxes(boxes, limitNumberOfTrucks);
            case 1 -> boxStackerStrategy.stackBoxes(trucksWithSize.get(0), boxes, limitNumberOfTrucks);
            default -> boxStackerStrategy.stackBoxes(trucksWithSize, boxes);
        };
        return truckRepository.save(resultTrucks);
    }

    private List<Box> fillAllBoxesFromStackingRequest(StackingRequest stackingRequest) {
        List<Box> allBoxes = new ArrayList<>();
        allBoxes.addAll(unpackBoxesFromTrucks(stackingRequest));
        allBoxes.addAll(boxFileParserService.parseBoxFile(stackingRequest.getBoxesFileName()));
        allBoxes.addAll(getBoxesByTheirName(stackingRequest));
        return allBoxes;
    }

    private List<Box> unpackBoxesFromTrucks(StackingRequest stackingRequest) {
        String trucksFileName = stackingRequest.getAlreadyFilledTrucksFileName();
        List<Truck> trucks = truckJsonFileParserService.parseFromFile(trucksFileName);
        return Convertor.unpackTrucks(trucks);
    }

    private List<Box> getBoxesByTheirName(StackingRequest stackingRequest) {
        List<String> boxNames = StackingRequestConvertor.convert(stackingRequest);
        return boxNames.stream()
                .map(boxService::getBoxByName)
                .toList();
    }


}

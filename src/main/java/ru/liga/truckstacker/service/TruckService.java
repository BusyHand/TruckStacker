package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.parser.StackingRequestParser;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.mapper.TruckMapper;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.repository.TruckRepository;
import ru.liga.truckstacker.service.boxgetter.StackingRequestBoxesGetter;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.enums.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, предоставляющий операции по укладке коробок и сохранению грузовиков.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TruckService {

    private final Map<Algorithm, BoxStackerStrategy> algorithmToBoxStackerStrategyMap;

    private final List<StackingRequestBoxesGetter> stackingRequestBoxGetters;

    private final StackingRequestParser stackingRequestParser;

    private final TruckRepository truckRepository;

    private final TruckMapper truckMapper;



    /**
     * Укладывает коробки в грузовики и сохраняет их в репозитории.
     *
     * @param stackingRequest Запрос на укладку, содержащий информацию о файлах грузовиков и коробок,
     *                        максимальном количестве грузовиков и типе алгоритма.
     * @return Список грузовиков, с которыми была выполнена укладка.
     */
    public List<TruckDto> stackBoxesAndSaveTrucks(StackingRequest stackingRequest) {
        List<Box> boxes = fillAllBoxesFromStackingRequest(stackingRequest);
        List<Truck> trucksWithSize = stackingRequestParser.parseTruckWithSize(stackingRequest);
        List<Truck> resultTrucks = stackBoxes(boxes, trucksWithSize, stackingRequest);
        truckRepository.saveAll(resultTrucks);
        return truckMapper.toDto(resultTrucks);
    }

    private List<Box> fillAllBoxesFromStackingRequest(StackingRequest stackingRequest) {
        return stackingRequestBoxGetters.stream()
                .flatMap(boxesGetter -> boxesGetter.getBoxes(stackingRequest).stream())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<Truck> stackBoxes(List<Box> boxes, List<Truck> trucksWithSize, StackingRequest stackingRequest) {
        Algorithm algorithm = Algorithm.fromString(stackingRequest.getTypeAlgorithmName());
        BoxStackerStrategy boxStackerStrategy = algorithmToBoxStackerStrategyMap.get(algorithm);
        int limitNumberOfTrucks = stackingRequest.getLimitNumberOfTrucks();
        return switch (trucksWithSize.size()) {
            case 0 -> boxStackerStrategy.stackBoxes(boxes, limitNumberOfTrucks);
            case 1 -> boxStackerStrategy.stackBoxes(trucksWithSize.get(0), boxes, limitNumberOfTrucks);
            default -> boxStackerStrategy.stackBoxes(trucksWithSize, boxes);
        };
    }
}

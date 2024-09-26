package ru.liga.truckstacker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.ConsoleService;

import java.util.Collections;
import java.util.List;

/**
 * Класс, обрабатывающий запросы на укладку коробок и сохранение грузовиков.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ConsoleController {

    private final ConsoleService consoleService;

    /**
     * Обрабатывает запрос на укладку коробок и сохранение грузовиков.
     * @param request Запрос на укладку, содержащий информацию о файлах грузовиков и коробок,
     *                максимальном количестве грузовиков и типе алгоритма.
     * @return Список грузовиков, с которыми была выполнена укладка.
     */
    public List<Truck> stackBoxesAndSaveTrucks(StackingRequest request) {
        try {
            log.info("Request received to stack boxes. Trucks file: {}, Boxes file: {}, Max number of trucks: {}, Algorithm type: {}",
                    request.getTrucksFileName(),
                    request.getBoxesFileName(),
                    request.getMaxSizeNumberOfTrucks(),
                    request.getTypeAlgorithmName());

            List<Truck> resultTrucks = consoleService.stackBoxesAndSaveTrucks(request);

            log.info("Box stacking and saving completed. Total trucks saved: {}", resultTrucks.size());
            return resultTrucks;
        } catch (Exception e) {
            log.error("Some error was thrown: {}", e.getMessage());
            System.err.println("Some error was thrown: " +  e.getMessage());
            return Collections.emptyList();
        }
    }
}

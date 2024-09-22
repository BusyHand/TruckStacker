package ru.liga.truckstacker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.ConsoleService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ConsoleController {

    private final ConsoleService consoleService;

    public List<Truck> stackBoxesAndSaveTrucks(StackingRequest request) {
        log.info("Request received to stack boxes. Trucks file: {}, Boxes file: {}, Max number of trucks: {}, Algorithm type: {}",
                request.getTrucksFileName(),
                request.getBoxesFileName(),
                request.getMaxSizeNumberOfTrucks(),
                request.getTypeAlgorithmName());

        List<Truck> resultTrucks = consoleService.stackBoxesAndSaveTrucks(request);

        log.info("Box stacking and saving completed. Total trucks saved: {}", resultTrucks.size());
        return resultTrucks;
    }
}

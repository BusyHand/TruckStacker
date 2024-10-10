package ru.liga.truckstacker.service.boxgetter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.parser.TruckJsonFileParser;
import ru.liga.truckstacker.service.boxgetter.StackingRequestBoxesGetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

/**
 * Реализация {@link StackingRequestBoxesGetter},
 * которая распаковывает коробки из грузовиков которые отправил пользователь в файле json.
 */
@Component
@RequiredArgsConstructor
public class StackingRequestBoxesGetterFromTruckFile implements StackingRequestBoxesGetter {

    private final TruckJsonFileParser truckJsonFileParser;

    @Override
    public List<Box> getBoxes(StackingRequest stackingRequest) {
        String trucksFileName = stackingRequest.getTrucksFileName();
        List<Truck> trucks = truckJsonFileParser.parseFromFile(trucksFileName);
        return unpackTrucks(trucks);
    }

    private List<Box> unpackTrucks(List<Truck> truckList) {
        return truckList.stream()
                .map(this::unpackTrucks)
                .flatMap(List::stream)
                .collect(toCollection(ArrayList::new));
    }

    private List<Box> unpackTrucks(Truck truck) {
        return truck.getBoxesInTruck()
                .entrySet()
                .stream()
                .flatMap(entry -> Collections.nCopies(entry.getValue(), entry.getKey()).stream())
                .toList();
    }
}

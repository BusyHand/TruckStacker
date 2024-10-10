package ru.liga.truckstacker.util;

import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

@UtilityClass
public class Convertor {
    public List<Box> unpackTrucks(List<Truck> truckList) {
        return truckList.stream()
                .map(Convertor::unpackTrucks)
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

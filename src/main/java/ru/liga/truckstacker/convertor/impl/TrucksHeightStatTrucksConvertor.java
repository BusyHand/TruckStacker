package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatTruck;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Component
public class TrucksHeightStatTrucksConvertor implements Convertor<List<Truck>, List<HeightStatTruck>> {
    @Override
    public List<HeightStatTruck> convert(List<Truck> trucks) {
        return trucks.stream()
                .map(truck -> new HeightStatTruck(truck))
                .collect(toCollection(ArrayList::new));
    }
}

package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.strategy.balanced.model.AreaStatTruck;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static java.util.stream.Collectors.toCollection;

@Component
public class TrucksToPriorityQueueAreaStatTrucksConvertor implements Convertor<List<Truck>, PriorityQueue<AreaStatTruck>> {
    @Override
    public PriorityQueue<AreaStatTruck> convert(List<Truck> truckList) {
        return truckList.stream()
                .map(AreaStatTruck::new)
                .collect(toCollection(() -> new PriorityQueue<AreaStatTruck>(Comparator.reverseOrder())));
    }
}

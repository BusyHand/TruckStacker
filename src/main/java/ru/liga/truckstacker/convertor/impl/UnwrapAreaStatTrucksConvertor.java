package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.strategy.balanced.model.AreaStatTruck;

import java.util.List;
import java.util.PriorityQueue;

@Component
public class UnwrapAreaStatTrucksConvertor implements Convertor<PriorityQueue<AreaStatTruck>, List<Truck>> {


    @Override
    public List<Truck> convert(PriorityQueue<AreaStatTruck> truckPriorityQueue) {
        return truckPriorityQueue.stream()
                .map(AreaStatTruck::getTruck)
                .toList();
    }
}

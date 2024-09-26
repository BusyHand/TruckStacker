package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatTruck;

import java.util.List;

@Component
public class UnwrapHeightStatTrucksConvertor implements Convertor<List<HeightStatTruck>, List<Truck>> {

    @Override
    public List<Truck> convert(List<HeightStatTruck> boxWrapperCollections) {
        return boxWrapperCollections.stream()
                .map(HeightStatTruck::getTruck)
                .toList();
    }
}

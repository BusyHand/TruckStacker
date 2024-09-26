package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

@Component
public class BoxSingleTruckConvertor implements Convertor<Box, Truck> {
    @Override
    public Truck convert(Box box) {
        Truck truck = new Truck();
        truck.addBoxByCoordinates(0, 0, box);
        return truck;
    }
}

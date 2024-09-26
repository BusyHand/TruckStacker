package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

@Component
public class TrucksStringConvertor implements Convertor<List<Truck>, String> {

    public String convert(List<Truck> truckList) {
        StringBuilder sb = new StringBuilder();
        int truckNumber = 1;

        for (Truck truck : truckList) {
            sb.append("Truck ").append(truckNumber++).append('\n');
            sb.append(truck);
        }
        return sb.toString();
    }

}

package ru.liga.truckstacker.convertor.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonTrucksConvertor implements Convertor<String, List<Truck>> {

    private final ObjectMapper objectMapper;
    private static final TypeReference<List<Truck>> TRUCK_LIST_TYPE = new TypeReference<>() {
    };

    @Override
    public List<Truck> convert(String trucksJson) {
        try {
            List<Truck> trucks = objectMapper.readValue(trucksJson, TRUCK_LIST_TYPE);
            log.info("Successfully converted to {} trucks", trucks.size());
            return trucks;
        } catch (JsonProcessingException e) {
            log.info("Error during parse truck json: {}", trucksJson, e.getMessage());
            throw new RuntimeException("Error during parse truck json: {}", e);
        }
    }
}

package ru.liga.truckstacker.service.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.model.Truck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrucksReaderConvertorService {
    private final ObjectMapper objectMapper;
    private static final TypeReference<List<Truck>> TRUCK_LIST_TYPE = new TypeReference<>() {};

    public List<Truck> readAndConvertToTruckList(String filename) {
        Path boxesInputPath = Path.of(filename);
        log.info("Starting to read and convert trucks from file: {}", filename);

        try {
            String trucksJson = Files.readString(boxesInputPath, StandardCharsets.UTF_8);
            List<Truck> trucks = objectMapper.readValue(trucksJson, TRUCK_LIST_TYPE);
            log.info("Successfully converted to {} trucks", trucks.size());
            return trucks;
        } catch (IOException e) {
            log.error("Input file {} was closed due to an error: ", boxesInputPath, e);
            throw new RuntimeException("Error reading or converting file", e);
        }
    }
}

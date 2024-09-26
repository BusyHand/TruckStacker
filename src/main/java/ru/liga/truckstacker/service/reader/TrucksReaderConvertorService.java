package ru.liga.truckstacker.service.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Truck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для чтения и преобразования списка грузовиков из файла.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrucksReaderConvertorService {

    private final Convertor<String, List<Truck>> stringTrucksConvertor;

    /**
     * Читает данные о грузовиках из файла и преобразует их в список объектов Truck.
     * @param filename Имя файла, из которого будут читать данные о грузовиках.
     * @return Список объектов Truck, считанных из файла.
     */
    public List<Truck> readAndConvertToTruckList(String filename) {
        Path boxesInputPath = Path.of(filename);
        log.info("Starting to read and convert trucks from file: {}", filename);
        try {
            String trucksJson = Files.readString(boxesInputPath, StandardCharsets.UTF_8);
            return stringTrucksConvertor.convert(trucksJson);
        } catch (IOException e) {
            log.error("Input file {} was closed due to an error: ", boxesInputPath, e);
            throw new RuntimeException("Error reading or converting file", e);
        }
    }
}

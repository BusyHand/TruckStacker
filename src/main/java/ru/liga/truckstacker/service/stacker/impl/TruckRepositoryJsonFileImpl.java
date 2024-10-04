package ru.liga.truckstacker.service.stacker.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.liga.truckstacker.repository.truck.TruckRepository;
import ru.liga.truckstacker.util.TruckRepositoryFilePathGenerator;
import ru.liga.truckstacker.model.Truck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
/**
 * Реализация репозитория для сохранения грузовиков в JSON-файле.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class TruckRepositoryJsonFileImpl implements TruckRepository {

    private final ObjectMapper objectMapper;
    /**
     * Сохраняет список грузовиков в JSON-файл.
     * @param truckList Список грузовиков для сохранения.
     * @return Сохраненный список грузовиков.
     */
    @Override
    public List<Truck> save(List<Truck> truckList) {
        log.info("Starting to save {} trucks.", truckList.size());
        try {
            Path filePath = TruckRepositoryFilePathGenerator.generateTruckFilePath();
            Files.createDirectories(filePath.getParent());
            objectMapper.writeValue(filePath.toFile(), truckList);
            log.info("Successfully saved trucks to {}", filePath);
            return truckList;
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for trucks: {}", e.getMessage());
            throw new RuntimeException("Error processing JSON", e);
        } catch (IOException e) {
            log.error("Error writing trucks to file: {}", e.getMessage());
            throw new RuntimeException("Error writing file", e);
        }
    }
}

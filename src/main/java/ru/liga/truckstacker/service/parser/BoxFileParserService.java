package ru.liga.truckstacker.service.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.reader.FileReader;

import java.util.Collections;
import java.util.List;

/**
 * Сервис для парсинга файлов с коробками.
 */
@Service
@RequiredArgsConstructor
public class BoxFileParserService {

    private static final TypeReference<List<Box>> BOX_LIST_TYPE = new TypeReference<>() {
    };

    private final FileReader fileReader;
    private final ObjectMapper objectMapper;

    /**
     * Парсит файл с коробками, указанный по имени файла.
     *
     * @param fileName Имя файла для парсинга.
     * @return Список объектов Box, извлеченных из файла.
     */
    public List<Box> parseBoxFile(String fileName) {
        if (fileName.isBlank()) {
            return Collections.emptyList();
        }
        String jsonBoxes = fileReader.readAllLines(fileName);
        return getBoxList(jsonBoxes);
    }

    private List<Box> getBoxList(String trucksJson) {
        try {
            return objectMapper.readValue(trucksJson, BOX_LIST_TYPE);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error during parse truck json: " + e.getMessage(), e);
        }
    }
}

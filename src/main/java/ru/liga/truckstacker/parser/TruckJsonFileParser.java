package ru.liga.truckstacker.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.mapper.BoxMapper;
import ru.liga.truckstacker.mapper.TruckMapper;
import ru.liga.truckstacker.reader.FileReader;
import ru.liga.truckstacker.service.BoxService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.liga.truckstacker.util.GlobalSettings.EMPTY_CELL;

/**
 * Сервис для парсинга файлов JSON с грузовиками.
 */
@Component
@RequiredArgsConstructor
public class TruckJsonFileParser {

    private static final TypeReference<List<TruckDto>> TRUCK_LIST_TYPE = new TypeReference<>() {
    };

    private final BoxService boxService;
    private final FileReader fileReader;
    private final ObjectMapper objectMapper;

    private final BoxMapper boxMapper;

    private final TruckMapper truckMapper;


    /**
     * Парсит файл JSON и извлекает список грузовиков.
     *
     * @param fileName Имя файла для парсинга.
     * @return Список объектов Truck, извлеченных из файла.
     */
    public List<Truck> parseFromFile(String fileName) {
        if (fileName.isBlank()) {
            return Collections.emptyList();
        }
        String jsonTrucks = fileReader.readAllLines(fileName);
        return getBoxesFromTruckArea(jsonTrucks);
    }

    private List<Truck> getBoxesFromTruckArea(String trucksJson) {
        try {
            List<TruckDto> trucksDto = objectMapper.readValue(trucksJson, TRUCK_LIST_TYPE);
            List<Truck> trucks = truckMapper.toEntity(trucksDto);
            trucks.forEach(truck ->
                    truck.setBoxesInTruck(
                            getBoxesFromTruckArea(
                                    truck.getTruckArea()
                            )
                    )
            );
            return trucks;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during parse truck json:", e);
        }
    }

    private Map<Box, Integer> getBoxesFromTruckArea(char[][] truckArea) {
        return parseTruckArea(truckArea);
    }

    private Map<Box, Integer> parseTruckArea(char[][] truckArea) {
        boolean[][] visited = new boolean[truckArea.length][truckArea[0].length];
        Map<Box, Integer> boxIntegerHashMap = new HashMap<>();

        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                char boxValue = truckArea[i][j];
                if (boxValue != EMPTY_CELL && !visited[i][j]) {
                    Box box = getBoxTypeByValue(boxValue);
                    boxIntegerHashMap.merge(box, 1, Integer::sum);
                    markBoxAsVisited(i, j, box, visited);
                }
            }
        }
        return boxIntegerHashMap;
    }

    private void markBoxAsVisited(int startRow, int startCol, Box box, boolean[][] visited) {
        int height = box.getHeight();
        int width = box.getWidth();

        for (int row = startRow; row < startRow + height; row++) {
            for (int col = startCol; col < startCol + width; col++) {
                if (row < visited.length && col < visited[0].length) {
                    visited[row][col] = true;
                }
            }
        }
    }

    private Box getBoxTypeByValue(char symbol) {
        List<BoxDto> allBoxes = boxService.getAllBoxes();
        List<Box> boxes = boxMapper.toEntity(allBoxes);
        for (Box box : boxes) {
            if (box.getSymbol() == symbol) {
                return box;
            }
        }
        throw new BoxNotFoundException("Box with this symbol: \"" + symbol + "\" not contains");
    }
}

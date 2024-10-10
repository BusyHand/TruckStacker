package ru.liga.truckstacker.parser;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Truck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Парсер запросов на укладку, который позволяет извлекать информацию о коробках
 * и грузовиках из запроса укладки.
 */
@Component
public class StackingRequestParser {
    /**
     * Конвертирует строку имен коробок из запроса на укладку в список строк.
     *
     * @param stackingRequest Запрос на укладку, содержащий имена коробок.
     * @return Список имен коробок, или пустой список, если имена коробок отсутствуют.
     */
    public List<String> getBoxNamesList(StackingRequest stackingRequest) {
        String boxNames = stackingRequest.getBoxNames();
        if (boxNames.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(boxNames.split(","))
                .map(String::trim)
                .toList();
    }

    /**
     * Получает список грузовиков на основе заданного размера из запроса на укладку.
     *
     * @param stackingRequest Запрос на укладку, содержащий размеры грузовиков.
     * @return Список грузовиков, созданных на основе указанных размеров, или пустой список, если размеры отсутствуют.
     */
    public List<Truck> parseTruckWithSize(StackingRequest stackingRequest) {
        String truckSize = stackingRequest.getTruckSize();
        if (truckSize.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(truckSize.split(","))
                .map(this::parse)
                .map(rec -> new Truck(rec.height, rec.width()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private record HeightAndWidth(int height, int width) {
    }

    private HeightAndWidth parse(String size) {
        String[] heightAndWidth = size.split("x");
        int height = Integer.parseInt(heightAndWidth[0].trim());
        int width = Integer.parseInt(heightAndWidth[1].trim());
        return new HeightAndWidth(height, width);
    }
}

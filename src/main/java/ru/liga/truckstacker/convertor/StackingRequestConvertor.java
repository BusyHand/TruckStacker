package ru.liga.truckstacker.convertor;

import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.model.Truck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StackingRequestConvertor {
    /**
     * Конвертирует строку имен коробок из запроса на укладку в список строк.
     *
     * @param stackingRequest Запрос на укладку, содержащий имена коробок.
     * @return Список имен коробок, или пустой список, если имена коробок отсутствуют.
     */
    public List<String> convert(StackingRequest stackingRequest) {
        String boxNames = stackingRequest.getBoxNames();
        if (boxNames.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(boxNames.split(","))
                .map(String::trim)
                .toList();
    }

    private record HeightAndWidth(int height, int width) {
    }

    /**
     * Получает список грузовиков на основе заданного размера из запроса на укладку.
     *
     * @param stackingRequest Запрос на укладку, содержащий размеры грузовиков.
     * @return Список грузовиков, созданных на основе указанных размеров, или пустой список, если размеры отсутствуют.
     */
    public List<Truck> getTrucksBySize(StackingRequest stackingRequest) {
        String truckSize = stackingRequest.getTruckSize();
        if (truckSize.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(truckSize.split(","))
                .map(StackingRequestConvertor::parse)
                .map(rec -> new Truck(rec.height, rec.width()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private HeightAndWidth parse(String size) {
        String[] heightAndWidth = size.split("x");
        int height = Integer.parseInt(heightAndWidth[0].trim());
        int width = Integer.parseInt(heightAndWidth[1].trim());
        return new HeightAndWidth(height, width);
    }
}

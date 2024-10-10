package ru.liga.truckstacker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.mapper.custom.TruckDtoDeserializer;
import ru.liga.truckstacker.mapper.custom.TruckDtoSerializer;

import java.util.Map;

@Data
@JsonDeserialize(using = TruckDtoDeserializer.class)
@JsonSerialize(using = TruckDtoSerializer.class)
public class TruckDto {

    @JsonProperty("truckArea")
    private char[][] truckArea;

    @JsonProperty("boxesInTruck")
    private Map<Box, Integer> boxesInTruck;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        char[][] truckArea = getTruckArea();
        sb.append('\n');
        for (int i = 0; i < truckArea.length ; i++) {
            sb.append('+');

            for (int j = 0; j < truckArea[0].length; j++) {
                sb.append(truckArea[i][j]);
            }

            sb.append('+')
                    .append('\n');
        }

        for (int i = 0; i < truckArea[0].length + 2; i++) {
            sb.append('+');
        }
        sb.append('\n')
                .append('\n');

        for (var boxIntegerEntry : boxesInTruck.entrySet()) {
            Box box = boxIntegerEntry.getKey();
            sb.append(box.getName())
                    .append('\n')
                    .append("form: ")
                    .append('\n')
                    .append(box.getForm())
                    .append('\n')
                    .append("Count: ")
                    .append(boxIntegerEntry.getValue())
                    .append('\n')
                    .append('\n');
        }
        return sb.toString();
    }

}

package ru.liga.truckstacker.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.liga.truckstacker.dto.TruckDto;

import java.io.IOException;

public class TruckDtoDeserializer extends JsonDeserializer {
    @Override
    public TruckDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        TruckDto truckDto = new TruckDto();
        JsonNode truckAreaNode = node.get("truckArea");
        int height = truckAreaNode.size();
        int width = truckAreaNode.get(0).size();
        char[][] truckArea = new char[height][width];
        for (int i = 0; i < truckAreaNode.size(); i++) {
            JsonNode jsonArray = truckAreaNode.get(i);
            for (int j = 0; j < jsonArray.size(); j++) {
                JsonNode jsonNode = jsonArray.get(j);
                truckArea[i][j] = jsonNode.asText().toCharArray()[0];
            }
        }
        truckDto.setTruckArea(truckArea);
        return truckDto;
    }
}

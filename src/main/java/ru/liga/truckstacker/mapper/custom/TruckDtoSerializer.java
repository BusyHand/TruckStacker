package ru.liga.truckstacker.mapper.custom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;

import java.io.IOException;
import java.util.Map;

public class TruckDtoSerializer extends JsonSerializer<TruckDto> {

    @Override
    public void serialize(TruckDto truckDto, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeFieldName("truckArea");
        gen.writeStartArray();
        for (char[] row : truckDto.getTruckArea()) {
            gen.writeString(new String(row));
        }
        gen.writeEndArray();

        gen.writeFieldName("boxesInTruck");
        gen.writeStartArray();
        for (Map.Entry<Box, Integer> entry : truckDto.getBoxesInTruck().entrySet()) {
            gen.writeStartObject();

            gen.writeObjectFieldStart("box");
            gen.writeStringField("name", entry.getKey().getName());
            gen.writeStringField("form", entry.getKey().getForm());
            gen.writeStringField("symbol", String.valueOf(entry.getKey().getSymbol()));
            gen.writeEndObject();

            gen.writeNumberField("count", entry.getValue());
            gen.writeEndObject();
        }
        gen.writeEndArray();

        gen.writeEndObject();
    }
}

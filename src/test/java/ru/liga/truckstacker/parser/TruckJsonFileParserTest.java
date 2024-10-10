package ru.liga.truckstacker.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TruckJsonFileParserTest {

    private TruckJsonFileParser parser;
    private BoxService boxService;
    private FileReader fileReader;
    private ObjectMapper objectMapper;
    private BoxMapper boxMapper;
    private TruckMapper truckMapper;

    @BeforeEach
    void setUp() {
        boxService = Mockito.mock(BoxService.class);
        fileReader = Mockito.mock(FileReader.class);
        objectMapper = new ObjectMapper();
        boxMapper = Mockito.mock(BoxMapper.class);
        truckMapper = Mockito.mock(TruckMapper.class);

        parser = new TruckJsonFileParser(boxService, fileReader, objectMapper, boxMapper, truckMapper);
    }

    @Test
    void testParseFromFile_EmptyFileName_ReturnsEmptyList() {
        List<Truck> trucks = parser.parseFromFile("");
        assertThat(trucks).isEmpty();
    }

    @Test
    void testParseFromFile_InvalidJson_ThrowsRuntimeException() {
        when(fileReader.readAllLines("invalid.json")).thenReturn("Invalid JSON");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> parser.parseFromFile("invalid.json"));
        assertThat(exception).hasMessageStartingWith("Error during parse truck json:");
    }


}

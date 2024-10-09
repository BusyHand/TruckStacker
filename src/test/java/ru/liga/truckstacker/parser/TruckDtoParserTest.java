package ru.liga.truckstacker.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.dto.TruckDto;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TruckDtoParserTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testTruckDtoDeserialization() throws Exception {
        // Example JSON string
        String json = """
                [
                  {
                    "truckArea": [
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      ["1", "1", "1", "1", "1", " "]
                    ]
                  }
                ]""";
        // Deserialize JSON to TruckDto
        TruckDto[] truckDtos = objectMapper.readValue(json, TruckDto[].class);

        // Assert that the deserialized object is not null
        assertNotNull(truckDtos);
        assertNotNull(truckDtos[0]);

        // Assert that the truckArea is correctly deserialized
        char[][] expectedTruckArea = {
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {'1', '1', '1', '1', '1', ' '}
        };

        // Validate the truckArea
        assertArrayEquals(expectedTruckArea, truckDtos[0].getTruckArea());
    }
}

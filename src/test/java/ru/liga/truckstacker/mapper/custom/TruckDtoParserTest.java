package ru.liga.truckstacker.mapper.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void testSerialization() throws Exception {
        // Arrange
        char[][] truckArea = {{'A', 'A'}, {'B', 'B'}};
        Map<Box, Integer> boxesInTruck = new HashMap<>();
        Box boxA = new Box(); // Assuming Box class has a default constructor
        boxA.setName("BoxA");
        boxA.setForm("FormA");
        boxesInTruck.put(boxA, 2);

        TruckDto truckDto = new TruckDto();
        truckDto.setTruckArea(truckArea);
        truckDto.setBoxesInTruck(boxesInTruck);

        // Act
        String json = objectMapper.writeValueAsString(truckDto);

        // Assert
        assertThat(json).contains("truckArea");
        assertThat(json).contains("boxesInTruck");
        assertThat(json).contains("BoxA");
    }

    @Test
    void testDeserialization() throws Exception {
        // Arrange
        String json = "{\"truckArea\":[[\"A\",\"A\"],[\"B\",\"B\"]],\"boxesInTruck\":{\"BoxA\":2}}";

        // Act
        TruckDto truckDto = objectMapper.readValue(json, TruckDto.class);

        // Assert
        assertThat(truckDto).isNotNull();
        assertThat(truckDto.getTruckArea()).isNotNull();
        assertThat(truckDto.getTruckArea().length).isEqualTo(2);
        assertThat(truckDto.getTruckArea()[0][0]).isEqualTo('A');
    }

    @Test
    void testToString() {
        // Arrange
        char[][] truckArea = {{'A', 'A'}, {'B', 'B'}};
        Map<Box, Integer> boxesInTruck = new HashMap<>();
        Box boxA = new Box(); // Assuming Box class has the necessary methods
        boxA.setName("BoxA");
        boxA.setForm("FormA");
        boxesInTruck.put(boxA, 2);

        TruckDto truckDto = new TruckDto();
        truckDto.setTruckArea(truckArea);
        truckDto.setBoxesInTruck(boxesInTruck);

        // Act
        String truckDtoString = truckDto.toString();

        // Assert
        assertThat(truckDtoString).contains("+");
        assertThat(truckDtoString).contains("BoxA");
        assertThat(truckDtoString).contains("form: \nFormA");
        assertThat(truckDtoString).contains("Count: 2");
    }
}

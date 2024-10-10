package ru.liga.truckstacker.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StackingRequestParserTest {

    private StackingRequestParser stackingRequestParser;

    @BeforeEach
    void setUp() {
        stackingRequestParser = new StackingRequestParser();
    }

    @Test
    void testGetBoxNamesList_ValidInput_ReturnsBoxNames() {
        // Arrange
        StackingRequest request = StackingRequest.builder()
                .boxNames("Box1, Box2, Box3")
                .build();

        // Act
        List<String> boxNames = stackingRequestParser.getBoxNamesList(request);

        // Assert
        assertThat(boxNames).containsExactly("Box1", "Box2", "Box3");
    }

    @Test
    void testGetBoxNamesList_EmptyInput_ReturnsEmptyList() {
        // Arrange
        StackingRequest request = StackingRequest.builder()
                .boxNames("")
                .build();

        // Act
        List<String> boxNames = stackingRequestParser.getBoxNamesList(request);

        // Assert
        assertThat(boxNames).isEmpty();
    }

    @Test
    void testParseTruckWithSize_ValidInput_ReturnsTrucks() {
        // Arrange
        StackingRequest request = StackingRequest.builder()
                .truckSize("2x3, 4x5")
                .build();

        // Act
        List<Truck> trucks = stackingRequestParser.parseTruckWithSize(request);

        // Assert
        assertThat(trucks).hasSize(2);
        assertThat(trucks).extracting("height").containsExactly(2, 4);
        assertThat(trucks).extracting("width").containsExactly(3, 5);
    }

    @Test
    void testParseTruckWithSize_EmptyInput_ReturnsEmptyList() {
        // Arrange
        StackingRequest request = StackingRequest.builder()
                .truckSize("")
                .build();

        // Act
        List<Truck> trucks = stackingRequestParser.parseTruckWithSize(request);

        // Assert
        assertThat(trucks).isEmpty();
    }

    @Test
    void testParseTruckWithSize_InvalidInput_ThrowsNumberFormatException() {
        // Arrange
        StackingRequest request = StackingRequest.builder()
                .truckSize("2x3, ABC")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> stackingRequestParser.parseTruckWithSize(request))
                .isInstanceOf(NumberFormatException.class);
    }
}

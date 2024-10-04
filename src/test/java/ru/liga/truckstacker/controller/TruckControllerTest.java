package ru.liga.truckstacker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.TruckService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TruckControllerTest {

    @MockBean
    private TruckService truckService;

    @Autowired
    private TruckController truckController;

    @Test
    void testStackBoxesAndSaveTrucks_withDefaultOptions() {
        // Given
        StackingRequest stackingRequest = StackingRequest.builder()
                .alreadyFilledTrucksFileName("")
                .boxesFileName("")
                .typeAlgorithmName("burke")
                .limitNumberOfTrucks(Integer.MAX_VALUE)
                .truckSize("")
                .boxNames("")
                .build();

        List<Truck> expectedTrucks = Collections.emptyList();
        when(truckService.stackBoxesAndSaveTrucks(stackingRequest)).thenReturn(expectedTrucks);

        // When
        List<Truck> result = truckController.stackBoxesAndSaveTrucks("", "", "burke", "", Integer.MAX_VALUE, "");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

    }

    @Test
    void testStackBoxesAndSaveTrucks_withCustomOptions() {
        // Given
        StackingRequest stackingRequest = StackingRequest.builder()
                .alreadyFilledTrucksFileName("trucks.txt")
                .boxesFileName("boxes.txt")
                .typeAlgorithmName("custom-algorithm")
                .limitNumberOfTrucks(5)
                .truckSize("large")
                .boxNames("box1,box2")
                .build();

        List<Truck> expectedTrucks = List.of(new Truck()); // You may need to add actual Truck properties here
        when(truckService.stackBoxesAndSaveTrucks(stackingRequest)).thenReturn(expectedTrucks);

        // When
        List<Truck> result = truckController.stackBoxesAndSaveTrucks(
                "trucks.txt", "boxes.txt", "custom-algorithm", "large", 5, "box1,box2");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);


    }

    @Test
    void testStackBoxesAndSaveTrucks_withInvalidTruckLimit() {
        // Test for an edge case: invalid input for `limitNumberOfTrucks`
        // Given
        StackingRequest stackingRequest = StackingRequest.builder()
                .alreadyFilledTrucksFileName("")
                .boxesFileName("")
                .typeAlgorithmName("burke")
                .limitNumberOfTrucks(-1)
                .truckSize("")
                .boxNames("")
                .build();

        when(truckService.stackBoxesAndSaveTrucks(stackingRequest)).thenThrow(new IllegalArgumentException("Invalid limit"));

        // When / Then
        try {
            truckController.stackBoxesAndSaveTrucks("", "", "burke", "", -1, "");
        } catch (IllegalArgumentException ex) {
            assertThat(ex).hasMessage("Invalid limit");
        }

    }
}

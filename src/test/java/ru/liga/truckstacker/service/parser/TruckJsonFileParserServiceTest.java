package ru.liga.truckstacker.service.parser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.BoxService;
import ru.liga.truckstacker.util.StartDataInDb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TruckJsonFileParserServiceTest {

    @Autowired
    private TruckJsonFileParserService truckJsonFileParserService;

    @Autowired
    private BoxService boxService;

    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        // Initialize necessary boxes before tests
        boxService.createBox(StartDataInDb.ONE.getBox());
        // Create a temporary file for JSON input
        tempFile = Files.createTempFile("trucks", ".json");
    }

    @AfterEach
    public void delete() throws IOException {
        // Clean up created box after tests
        boxService.deleteBoxByName(StartDataInDb.ONE.getBox().getName());
        // Optionally delete the temp file (it may be deleted automatically)
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testReadAndConvertToTruckList_Success() throws IOException {
        // Prepare test data for a single truck
        String trucksJson = """
                [
                    {
                        "truckArea": [
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            ["1", "1", " ", " ", " ", " "]
                        ]
                    }
                ]""";
        // Write the test data to the temporary file
        Files.writeString(tempFile, trucksJson);

        // Expected Truck setup
        Truck expectedTruck = new Truck(5, 6);
        expectedTruck.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        expectedTruck.addBoxByCoordinates(1, 0, StartDataInDb.ONE.getBox());
        List<Truck> expectedTrucks = List.of(expectedTruck);

        // Execute the method under test
        List<Truck> trucks = truckJsonFileParserService.parseFromFile(tempFile.toString());

        // Verify results
        assertThat(trucks).hasSize(1);
        assertThat(trucks.get(0).getTruckArea()).isEqualTo(expectedTrucks.get(0).getTruckArea());
        assertThat(trucks.get(0).getBoxesInTruck()).isEqualTo(expectedTrucks.get(0).getBoxesInTruck());
    }

    @Test
    void testReadAndConvertToTruckList_SuccessMultiple() throws IOException {
        // Prepare test data for multiple trucks
        String trucksJson = """
                [
                    {
                        "truckArea": [
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            ["1", "1", " ", " ", " ", " "]
                        ]
                    },
                    {
                        "truckArea": [
                            [" ", " ", " ", " ", " ", " "],
                            [" ", " ", " ", " ", " ", " "],
                            ["1", " ", "1", " ", " ", " "]
                        ]
                    }
                ]""";
        // Write the test data to the temporary file
        Files.writeString(tempFile, trucksJson);

        // Expected Truck setup for both trucks
        Truck expectedTruck1 = new Truck(5, 6);
        expectedTruck1.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        expectedTruck1.addBoxByCoordinates(1, 0, StartDataInDb.ONE.getBox());

        Truck expectedTruck2 = new Truck(3, 6);
        expectedTruck2.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        expectedTruck2.addBoxByCoordinates(2, 0, StartDataInDb.ONE.getBox());

        List<Truck> expectedTrucks = List.of(expectedTruck1, expectedTruck2);

        // Execute the method under test
        List<Truck> trucks = truckJsonFileParserService.parseFromFile(tempFile.toString());

        // Verify results for both trucks
        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getTruckArea()).isEqualTo(expectedTrucks.get(0).getTruckArea());
        assertThat(trucks.get(0).getBoxesInTruck()).isEqualTo(expectedTrucks.get(0).getBoxesInTruck());
        assertThat(trucks.get(1).getTruckArea()).isEqualTo(expectedTrucks.get(1).getTruckArea());
        assertThat(trucks.get(1).getBoxesInTruck()).isEqualTo(expectedTrucks.get(1).getBoxesInTruck());
    }

    @Test
    void testReadAndConvertToTruckList_FileNotFound() {
        String invalidPath = "invalid/path/to/file.json";

        // Assert that a RuntimeException is thrown when the file doesn't exist
        assertThatThrownBy(() -> truckJsonFileParserService.parseFromFile(invalidPath))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error reading or converting file");
    }

    @Test
    void testReadAndConvertToTruckList_InvalidJson() throws IOException {
        // Prepare an invalid JSON input
        String invalidJson = "not a json";
        Files.writeString(tempFile, invalidJson);

        // Assert that a RuntimeException is thrown for invalid JSON
        assertThatThrownBy(() -> truckJsonFileParserService.parseFromFile(tempFile.toString()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testReadAndConvertToTruckList_Success4() throws IOException {
        // Prepare test data
        String trucksJson = """
                [
                  {
                    "truckArea": [
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      ["1", "1", " ", " ", " ", " "]
                    ]
                  }
                ]""";
        Files.writeString(tempFile, trucksJson);

        Truck truck1 = new Truck(5, 6);
        truck1.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        truck1.addBoxByCoordinates(1, 0, StartDataInDb.ONE.getBox());
        List<Truck> mockTrucks = List.of(truck1);

        // Execute the method
        List<Truck> trucks = truckJsonFileParserService.parseFromFile(tempFile.toString());

        // Verify results
        assertThat(trucks).hasSize(1);
        assertThat(trucks.get(0).getTruckArea()).isEqualTo(mockTrucks.get(0).getTruckArea());
        assertThat(trucks.get(0).getBoxesInTruck()).isEqualTo(mockTrucks.get(0).getBoxesInTruck());
    }

    @Test
    void testReadAndConvertToTruckList_Success2() throws IOException {
        // Prepare test data
        String trucksJson = """
                [
                  {
                    "truckArea": [
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      ["1", "1", " ", " ", " ", " "]
                    ]
                  },
                  {
                    "truckArea": [
                      [" ", " ", " ", " ", " ", " "],
                      [" ", " ", " ", " ", " ", " "],
                      ["1", " ", "1", " ", " ", " "]
                    ]
                  }
                ]""";
        Files.writeString(tempFile, trucksJson);

        // Mock ObjectMapper's behavior
        Truck truck1 = new Truck(5, 6);
        truck1.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        truck1.addBoxByCoordinates(1, 0, StartDataInDb.ONE.getBox());
        Truck truck2 = new Truck(3, 6);
        truck2.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox());
        truck2.addBoxByCoordinates(2, 0, StartDataInDb.ONE.getBox());
        List<Truck> mockTrucks = List.of(truck1, truck2);

        // Execute the method
        List<Truck> trucks = truckJsonFileParserService.parseFromFile(tempFile.toString());

        // Verify results
        assertThat(trucks).hasSize(2);
        assertThat(trucks.get(0).getTruckArea()).isEqualTo(mockTrucks.get(0).getTruckArea());
        assertThat(trucks.get(0).getBoxesInTruck()).isEqualTo(mockTrucks.get(0).getBoxesInTruck());
        assertThat(trucks.get(1).getTruckArea()).isEqualTo(mockTrucks.get(1).getTruckArea());
        assertThat(trucks.get(1).getBoxesInTruck()).isEqualTo(mockTrucks.get(1).getBoxesInTruck());
    }

    @Test
    void testReadAndConvertToTruckList_FileNotFound2() {
        String invalidPath = "invalid/path/to/file.json";

        // Assert that a RuntimeException is thrown
        assertThatThrownBy(() -> truckJsonFileParserService.parseFromFile(invalidPath))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error reading or converting file");
    }

    @Test
    void testReadAndConvertToTruckList_InvalidJson3() throws IOException {
        // Prepare an invalid JSON
        String invalidJson = "not a json";
        Files.writeString(tempFile, invalidJson);

        // Assert that a RuntimeException is thrown
        assertThatThrownBy(() -> truckJsonFileParserService.parseFromFile(tempFile.toString()))
                .isInstanceOf(RuntimeException.class);
    }
}

package ru.liga.truckstacker.service.reader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.reader.TrucksReaderConvertorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class TrucksReaderConvertorServiceTest {

    @Autowired
    private TrucksReaderConvertorService trucksReaderConvertorService;

    @Mock
    private ObjectMapper objectMapper;

    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        tempFile = Files.createTempFile("trucks", ".json");
    }

    @Test
    void testReadAndConvertToTruckList_Success() throws IOException {
        // Prepare test data
        String trucksJson = "[{\"truckArea\":[[0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0]]}]"; // Example JSON for a Truck
        Files.writeString(tempFile, trucksJson);

        // Mock ObjectMapper's behavior
        List<Truck> mockTrucks = List.of(new Truck());
        when(objectMapper.readValue(trucksJson, List.class)).thenReturn(mockTrucks);

        // Execute the method
        List<Truck> trucks = trucksReaderConvertorService.readAndConvertToTruckList(tempFile.toString());

        // Verify results
        assertThat(trucks).hasSize(1);
        assertThat(trucks.get(0)).isEqualTo(mockTrucks.get(0));
    }

    @Test
    void testReadAndConvertToTruckList_FileNotFound() {
        String invalidPath = "invalid/path/to/file.json";

        // Assert that a RuntimeException is thrown
        assertThatThrownBy(() -> trucksReaderConvertorService.readAndConvertToTruckList(invalidPath))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error reading or converting file");
    }

    @Test
    void testReadAndConvertToTruckList_InvalidJson() throws IOException {
        // Prepare an invalid JSON
        String invalidJson = "not a json";
        Files.writeString(tempFile, invalidJson);

        // Assert that a RuntimeException is thrown
        assertThatThrownBy(() -> trucksReaderConvertorService.readAndConvertToTruckList(tempFile.toString()))
                .isInstanceOf(RuntimeException.class);
    }
}

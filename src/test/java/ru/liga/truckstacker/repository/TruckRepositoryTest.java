package ru.liga.truckstacker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.FilePathGenerator;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.runner.MainCommandLineRunner;
import ru.liga.truckstacker.util.TruckBoxFiller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.liga.truckstacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truckstacker.config.GlobalSettings.WIDTH_TRUCK;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class TruckRepositoryTest {

    @Autowired
    private TruckRepositoryImpl truckRepositoryImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilePathGenerator filePathGenerator;

    private static final TypeReference<List<Truck>> TRUCK_LIST_TYPE = new TypeReference<>() {
    };


    @Test
    public void testSaveTruckListCreatesFile(@TempDir Path tempDir) throws IOException {
        // Create test trucks
        List<Truck> trucks = TruckBoxFiller.createFilledTrucks(2);

        // Mock the PathUtil to return a path in the temporary directory
        Path expectedPath = tempDir.resolve("trucks-" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "-1.json");
        Mockito.when(filePathGenerator.generateTruckFilePath()).thenReturn(expectedPath);

        truckRepositoryImpl.save(trucks);  // Save trucks

        // Validate that the file has been created in the temp directory
        assertThat(Files.exists(expectedPath)).isTrue();

        // Validate the contents of the file
        String jsonContent = Files.readString(expectedPath);
        List<Truck> savedTrucks = objectMapper.readValue(jsonContent, TRUCK_LIST_TYPE);
        assertThat(savedTrucks).hasSize(2);  // Ensure two trucks were saved
    }

    @Test
    public void testSaveTruckContent(@TempDir Path tempDir) throws IOException {
        // Create test trucks
        List<Truck> trucks = TruckBoxFiller.createFilledFullTrucks(2);

        // Mock the PathUtil to return a path in the temporary directory
        Path expectedPath = tempDir.resolve("trucks-" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "-1.json");
        Mockito.when(filePathGenerator.generateTruckFilePath()).thenReturn(expectedPath);

        truckRepositoryImpl.save(trucks);  // Save trucks

        // Check file and content
        String jsonContent = Files.readString(expectedPath);
        List<Truck> savedTrucks = objectMapper.readValue(jsonContent, TRUCK_LIST_TYPE);

        // Verify saved truck area
        Truck savedTruck = savedTrucks.get(0);
        assertThat(savedTruck.getTruckArea()).hasDimensions(HEIGHT_TRUCK, WIDTH_TRUCK);
    }
}

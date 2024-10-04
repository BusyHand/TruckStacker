package ru.liga.truckstacker.repository.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.liga.truckstacker.repository.box.dao.BoxFileDbDao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoxFileDbDaoTest {

    @MockBean
    private CommandLineRunner initBoxes;

    @Autowired
    private BoxFileDbDao boxFileDbDao;
    private Path dbPath = Path.of("db", "boxes", "boxes.txt");

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(dbPath.getParent());
        if (!Files.exists(dbPath)) {
            Files.createFile(dbPath);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // Cleanup the test database after each test
        Files.deleteIfExists(dbPath);
        Files.deleteIfExists(Path.of("temp-boxes.txt"));
    }

    @Test
    void testFileCreationOnWrite() throws IOException {
        // File should be created if it doesn't exist when using writeAround
        boxFileDbDao.writeAround(() -> "Box1\tForm1\tA");

        assertTrue(Files.exists(dbPath), "File should be created after writing.");
        List<String> lines = Files.readAllLines(dbPath);
        assertEquals(1, lines.size());
        assertEquals("Box1\tForm1\tA", lines.get(0));
    }

    @Test
    void testReadAroundWorksWithExistingFile() throws IOException {
        // Prepare test data
        Files.write(dbPath, List.of("Box1\tForm1\tA", "Box2\tForm2\tB"), StandardOpenOption.CREATE);

        List<String> result = boxFileDbDao.readAround(lines -> lines.toList());

        assertEquals(2, result.size());
        assertEquals("Box1\tForm1\tA", result.get(0));
        assertEquals("Box2\tForm2\tB", result.get(1));
    }

    @Test
    void testWritingAppendsCorrectly() throws IOException {
        // Write initial data and then append more
        boxFileDbDao.writeAround(() -> "Box1\tForm1\tA");
        boxFileDbDao.writeAround(() -> "Box2\tForm2\tB");

        List<String> lines = Files.readAllLines(dbPath);
        assertEquals(2, lines.size());
        assertEquals("Box1\tForm1\tA", lines.get(0));
        assertEquals("Box2\tForm2\tB", lines.get(1));
    }

    @Test
    void testReadAndWriteAround() throws IOException {
        // Prepare initial data
        Files.write(dbPath, List.of("Box1\tForm1\tA"), StandardOpenOption.CREATE);

        boxFileDbDao.readAndWriteAround((lines, writer) -> {
            lines.forEach(writer::println); // Retain current lines
            writer.println("Box2\tForm2\tB"); // Append a new line
        });

        List<String> newLines = Files.readAllLines(dbPath);
        assertEquals(2, newLines.size());
        assertTrue(newLines.contains("Box1\tForm1\tA"));
        assertTrue(newLines.contains("Box2\tForm2\tB"));
    }

    @Test
    void testErrorHandlingOnFileCreation() {
        assertThrows(RuntimeException.class, () -> {
            // Simulate an IOException when reading
            boxFileDbDao.readAround(lines -> {
                throw new RuntimeException("Forced exception"); // Simulate failure
            });
        });
    }
}

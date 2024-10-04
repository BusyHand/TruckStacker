package ru.liga.truckstacker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.model.Box;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoxServiceIntegrationTest {

    @Autowired
    private BoxService boxService;

    private final Path boxesFilePath = Path.of("db", "boxes", "boxes.txt");

    @BeforeEach
    public void setUp() throws IOException {
        // Clean the boxes file before each test
        if (Files.exists(boxesFilePath)) {
            Files.delete(boxesFilePath);
        }
        // Create the file to ensure it exists
        Files.createDirectories(boxesFilePath.getParent());
        Files.createFile(boxesFilePath);
    }

    @Test
    void testCreateAndRetrieveBox() {
        Box newBox = new Box("Box1", "##", 'X');
        boxService.createBox(newBox);

        Box retrievedBox = boxService.getBoxByName("Box1");
        assertThat(retrievedBox).isEqualTo(newBox);
    }

    @Test
    void testUpdateBox() {
        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);

        Box updatedBox = new Box("Box1", "##Updated", 'Y');
        boxService.updateBoxByName(updatedBox);

        Box retrievedBox = boxService.getBoxByName("Box1");
        assertThat(retrievedBox).isEqualTo(updatedBox);
    }

    @Test
    void testDeleteBox() {
        Box newBox = new Box("Box1", "##", 'X');
        boxService.createBox(newBox);

        // Assert that the box exists before deletion
        assertThat(boxService.getBoxByName("Box1")).isNotNull();

        boxService.deleteBoxByName("Box1");

        // Assert that the box is deleted
        assertThatThrownBy(() -> boxService.getBoxByName("Box1"))
                .isInstanceOf(BoxNotFoundException.class);
    }

    @Test
    void testGetAllBoxes() {
        Box box1 = new Box("Box1", "##", 'X');
        Box box2 = new Box("Box2", "##", 'Y');
        // Create boxes
        boxService.createBox(box1);
        boxService.createBox(box2);

        List<Box> allBoxes = boxService.getAllBoxes();
        assertThat(allBoxes).containsExactlyInAnyOrder(box1, box2);
    }
}
package ru.liga.truckstacker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.mapper.BoxMapper;
import ru.liga.truckstacker.repository.BoxRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class BoxServiceIntegrationTest {

    @Autowired
    private BoxService boxService;

    @Autowired
    private BoxRepository boxRepository;

    @BeforeEach
    public void setUp() {
        boxRepository.deleteAll();
    }

    @Test
    public void testCreateAndRetrieveBox() {
        // Arrange
        BoxDto boxDto = new BoxDto("SampleBox", "", "s");

        // Act
        boxService.createBox(boxDto);
        BoxDto retrievedBox = boxService.getBoxByName("SampleBox");

        // Assert
        assertNotNull(retrievedBox);
        assertEquals("SampleBox", retrievedBox.getName());
    }

    @Test
    public void testGetAllBoxes() {
        // Arrange
        Box box1 = new Box("Box1", "", 's');
        Box box2 = new Box("Box2", "", 's');
        boxRepository.save(box1);
        boxRepository.save(box2);

        // Act
        List<BoxDto> boxes = boxService.getAllBoxes();

        // Assert
        assertNotNull(boxes);
        assertEquals(2, boxes.size());
        assertTrue(boxes.stream().anyMatch(boxDto -> boxDto.getName().equals("Box1")));
        assertTrue(boxes.stream().anyMatch(boxDto -> boxDto.getName().equals("Box2")));
    }

    @Test
    public void testUpdateBox() {
        // Arrange
        BoxDto boxDto = new BoxDto("UpdateBox", "aa", "s");
        boxService.createBox(boxDto);

        BoxDto updatedBoxDto = new BoxDto("UpdateBox", "aa", "f"); // Same name for update

        // Act
        BoxDto result = boxService.updateBoxByName(updatedBoxDto);

        // Assert
        assertNotNull(result);
        assertEquals("UpdateBox", result.getName());
    }

    @Test
    public void testDeleteBox() {
        // Arrange
        BoxDto boxDto = new BoxDto("DeleteBox", "", "s");
        boxService.createBox(boxDto);

        // Act
        boxService.deleteBoxByName("DeleteBox");

        // Assert
        assertThrows(BoxNotFoundException.class, () -> boxService.getBoxByName("DeleteBox"));
    }
}

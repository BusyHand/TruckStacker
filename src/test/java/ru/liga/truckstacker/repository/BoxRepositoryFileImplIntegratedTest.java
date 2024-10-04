package ru.liga.truckstacker.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.exception.BoxWithThisNameAlreadyExistException;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.repository.box.impl.BoxRepositoryFileImpl;
import ru.liga.truckstacker.repository.box.dao.BoxFileDbDao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BoxRepositoryFileImplIntegratedTest {

    @Autowired
    private BoxFileDbDao boxFileDbDao;

    @Autowired
    private BoxRepositoryFileImpl boxRepository;

    private static final Path dbPath = Path.of("db", "boxes", "boxes.txt");

    @BeforeEach
    void setUp() throws Exception {
        if (Files.exists(dbPath)) {
            Files.delete(dbPath);
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        if (Files.exists(dbPath)) {
            Files.delete(dbPath);
        }
    }

    @Test
    void testGetAllBoxes() throws Exception {
        boxRepository.saveBox(new Box("Box1", "Fo \nr\nm1",'A'));
        boxRepository.saveBox(new Box("Box2", "Form1",'B'));

        List<Box> boxes = boxRepository.getAllBoxes();

        assertEquals(2, boxes.size());
        assertEquals("Box1", boxes.get(0).getName());
        assertEquals("AA \nA\nAA", boxes.get(0).getForm());
        assertEquals("Box2", boxes.get(1).getName());
        assertEquals("BBBBB", boxes.get(1).getForm());
    }

    @Test
    void testGetBoxByNameExists() throws Exception {
        boxRepository.saveBox(new Box("Box1", "Form1",'A'));

        Box box = boxRepository.getBoxByName("Box1");

        assertEquals("Box1", box.getName());
    }

    @Test
    void testSaveBoxNew() throws Exception {
        Box box = new Box("Box1", "Form1", 'A');

        boxRepository.saveBox(box);

        List<Box> boxes = boxRepository.getAllBoxes();

        assertEquals(1, boxes.size());
        assertEquals("Box1", boxes.get(0).getName());
    }

    @Test
    void testSaveBoxAlreadyExists() throws Exception {
        Box box = new Box("Box1", "Form1", 'A');
        boxRepository.saveBox(box); // Save the first box

        Box newBox = new Box("Box1", "Form2", 'B');
        assertThrows(BoxWithThisNameAlreadyExistException.class, () -> boxRepository.saveBox(newBox));
    }

    @Test
    void testUpdateBoxByName() throws Exception {
        Box box = new Box("Box1", "Form1", 'A');
        boxRepository.saveBox(box); // Save the initial box

        Box updatedBox = new Box("Box1", "UpdatedForm", 'B');
        boxRepository.updateBoxByName(updatedBox);

        Box retrievedBox = boxRepository.getBoxByName("Box1");

        assertEquals("BBBBBBBBBBB", retrievedBox.getForm());
        assertEquals('B', retrievedBox.getSymbol());
    }

    @Test
    void testUpdateAndAnotherAction() throws Exception {
        Box box = new Box("Box1", "Form1", 'A');
        boxRepository.saveBox(box); // Save the initial box

        Box updatedBox = new Box("Box1", "UpdatedForm", 'B');
        boxRepository.updateBoxByName(updatedBox);

        Box box2 = new Box("Box2", "Form1", 'A');
        boxRepository.saveBox(box2); // Save the initial box

        Box retrievedBox = boxRepository.getBoxByName("Box1");

        assertEquals("BBBBBBBBBBB", retrievedBox.getForm());
        assertEquals('B', retrievedBox.getSymbol());
    }

    @Test
    void testDeleteBoxByName() throws Exception {
        Box box = new Box("Box1", "Form1", 'A');
        boxRepository.saveBox(box); // Save the box

        boxRepository.deleteBoxByName("Box1");

        assertThrows(BoxNotFoundException.class, () -> boxRepository.getBoxByName("Box1"));
    }
}


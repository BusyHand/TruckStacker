package ru.liga.truckstacker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.BoxService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BoxControllerTest {

    @Mock
    private BoxService boxService;

    @InjectMocks
    private BoxController boxController;

    @Test
    void testGetAllBoxes() {
        Box box1 = new Box("Box1", "##", 'X');
        Box box2 = new Box("Box2", "##", 'Y');
        List<Box> boxes = List.of(box1, box2);

        when(boxService.getAllBoxes()).thenReturn(boxes);

        List<Box> result = boxController.getAllBoxes();

        assertEquals(boxes, result);
    }

    @Test
    void testGetBoxByName() {
        Box box = new Box("Box1", "##", 'X');
        String boxName = "Box1";

        when(boxService.getBoxByName(boxName)).thenReturn(box);

        Box result = boxController.getBoxByName(boxName);

        assertEquals(box, result);
    }

    @Test
    void testCreateBox() {
        String result = boxController.createBox("Box1", "##", "X");

        assertEquals("CREATED SUCCESS", result);
    }

    @Test
    void testUpdateBoxByName() {
        String result = boxController.updateBoxByName("Box1", "###", "Y");

        assertEquals("UPDATED SUCCESS", result);
    }

    @Test
    void testDeleteBoxByName() {
        String result = boxController.deleteBoxByName("Box1");

        assertEquals("DELETED SUCCESS", result);
    }
}

package ru.liga.truckstacker.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.controller.ConsoleController;
import ru.liga.truckstacker.model.Truck;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class UserInterfaceTest {
    @Autowired
    private UserInterface userInterface;

    @MockBean
    private ConsoleController consoleController;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restore original output stream
    }

    @Test
    void testStart() {
        // Arrange
        String input = "\ntrucks.json\nboxes.txt\nburke\n10\nexit\n"; // Simulated user input
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        List<Truck> mockTrucks = Collections.singletonList(new Truck());
        when(consoleController.request("trucks.json", "boxes.txt", 10, "burke"))
                .thenReturn(mockTrucks);

        // Act
        userInterface.start();

        // Assert
        String output = outputStream.toString();
        verify(consoleController).request("trucks.json", "boxes.txt", 10, "burke");

        // Check that the output contains the trucks display
        assertTrue(output.contains("Truck")); // Adjust this to check for actual truck display content
    }
}

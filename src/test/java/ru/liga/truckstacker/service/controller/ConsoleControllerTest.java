package ru.liga.truckstacker.service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.controller.ConsoleController;
import ru.liga.truckstacker.convertor.BoxType;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.reader.BoxReaderConvertorService;
import ru.liga.truckstacker.service.reader.TrucksReaderConvertorService;
import ru.liga.truckstacker.service.stacker.BoxStackerServiceFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class ConsoleControllerTest {
    @Autowired
    private ConsoleController consoleController;

    @MockBean
    private TrucksReaderConvertorService trucksReaderConvertorService;

    @MockBean
    private BoxReaderConvertorService boxReaderConvertorService;

    @MockBean
    private BoxStackerServiceFactory boxStackerServiceFactory;


    @Test
    void testRequest() {
        // Arrange
        String trucksFilename = "trucks.txt";
        String boxesFilename = "boxes.txt";
        int maxSize = 10;
        String typeAlgorithmName = "burke";

        List<Truck> mockTrucks = Collections.singletonList(new Truck());
        List<Box> mockBoxes = Collections.singletonList(BoxType.ONE.getBox());
        List<Truck> mockResultTrucks = Collections.singletonList(new Truck());

        when(trucksReaderConvertorService.readAndConvertToTruckList(trucksFilename)).thenReturn(mockTrucks);
        when(boxReaderConvertorService.readAndConvertToBoxList(boxesFilename)).thenReturn(mockBoxes);
        when(boxStackerServiceFactory.stackBoxesAndSaveTrucks(mockTrucks, maxSize, mockBoxes, typeAlgorithmName))
                .thenReturn(mockResultTrucks);

        // Act
        List<Truck> resultTrucks = consoleController.request(trucksFilename, boxesFilename, maxSize, typeAlgorithmName);

        // Assert
        assertEquals(mockResultTrucks, resultTrucks);
        verify(trucksReaderConvertorService).readAndConvertToTruckList(trucksFilename);
        verify(boxReaderConvertorService).readAndConvertToBoxList(boxesFilename);
        verify(boxStackerServiceFactory).stackBoxesAndSaveTrucks(mockTrucks, maxSize, mockBoxes, typeAlgorithmName);
    }
}

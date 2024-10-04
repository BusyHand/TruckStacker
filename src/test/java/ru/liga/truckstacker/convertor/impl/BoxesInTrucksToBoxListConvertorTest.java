package ru.liga.truckstacker.convertor.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.util.StartDataInDb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BoxesInTrucksToBoxListConvertorTest {


    private Truck truckWithSingleBox;
    private Truck truckWithMultipleBoxes;
    private Truck emptyTruck;

    @BeforeEach
    public void setUp() {
        truckWithSingleBox = new Truck();
        truckWithSingleBox.addBoxByCoordinates(0, 0, StartDataInDb.ONE.getBox()); // Add one box type

        truckWithMultipleBoxes = new Truck();
        truckWithMultipleBoxes.addBoxByCoordinates(0, 0, StartDataInDb.TWO.getBox()); // Add two box type
        truckWithMultipleBoxes.addBoxByCoordinates(1, 0, StartDataInDb.THREE.getBox()); // Add three box type
        truckWithMultipleBoxes.addBoxByCoordinates(1, 1, StartDataInDb.THREE.getBox()); // Add another three box type

        emptyTruck = new Truck(); // Empty truck, no boxes added
    }

    @Test
    public void testConvertSingleTruckWithOneBox() {
        List<Truck> truckList = Collections.singletonList(truckWithSingleBox);
        List<Box> result = Convertor.unpackTrucks(truckList);

        assertEquals(1, result.size());
        assertEquals(StartDataInDb.ONE.getBox(), result.get(0));
    }

    @Test
    public void testConvertTruckWithMultipleBoxes() {
        List<Truck> truckList = Collections.singletonList(truckWithMultipleBoxes);
        List<Box> result =  Convertor.unpackTrucks(truckList);

        // The expected list should contain:
        // 1 * BoxType.TWO and 2 * BoxType.THREE
        List<Box> expected = new ArrayList<>();
        expected.add(StartDataInDb.TWO.getBox());
        expected.add(StartDataInDb.THREE.getBox());
        expected.add(StartDataInDb.THREE.getBox()); // Two instances of BoxType.THREE

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected), "Result should contain all expected boxes.");
    }

    @Test
    public void testConvertEmptyTruck() {
        List<Truck> truckList = Collections.singletonList(emptyTruck);
        List<Box> result = Convertor.unpackTrucks(truckList);

        assertEquals(0, result.size()); // Should return an empty list
    }

    @Test
    public void testConvertMultipleTrucks() {
        List<Truck> truckList = Arrays.asList(truckWithSingleBox, truckWithMultipleBoxes);
        List<Box> result = Convertor.unpackTrucks(truckList);

        List<Box> expected = new ArrayList<>();
        expected.add(StartDataInDb.ONE.getBox());
        expected.add(StartDataInDb.TWO.getBox());
        expected.add(StartDataInDb.THREE.getBox());
        expected.add(StartDataInDb.THREE.getBox());

        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected), "Result should contain all expected boxes.");
    }

    @Test
    public void testAllBoxesArePresentInResult() {
        List<Truck> truckList = Arrays.asList(truckWithSingleBox, truckWithMultipleBoxes);
        List<Box> result = Convertor.unpackTrucks(truckList);

        List<Box> expected = new ArrayList<>();
        expected.add(StartDataInDb.ONE.getBox());
        expected.add(StartDataInDb.TWO.getBox());
        expected.add(StartDataInDb.THREE.getBox());
        expected.add(StartDataInDb.THREE.getBox());

        // Check that all boxes are present in the result
        for (Box box : expected) {
            long countInResult = result.stream().filter(b -> b.equals(box)).count();
            long countInExpected = expected.stream().filter(b -> b.equals(box)).count();
            assertEquals(countInExpected, countInResult, "There should be " + countInExpected + " instances of " + box);
        }
    }
}

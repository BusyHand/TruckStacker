package ru.liga.truckstacker.service.stacker.impl.burke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.exception.BoxCantStackInTruckException;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BurkeBoxStackerStrategyTest {

    BurkeBoxStackerStrategy burkeBoxStackerStrategy = new BurkeBoxStackerStrategy();

    Truck truckExample = new Truck(5, 5);


    @Test
    void testStackBoxesWithEmptyBoxList() {
        List<Box> emptyBoxList = Collections.emptyList();
        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(truckExample, emptyBoxList, 3);

        // Since there are no boxes, no trucks should be used
        assertEquals(0, resultTrucks.size());
    }

    @Test
    void testStackBoxesWithSingleBox() {
        Box box = new Box("Box1", "X\nX", 'X');  // 1x2 Box
        List<Box> singleBoxList = List.of(box);
        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(truckExample, singleBoxList, 1);

        // Assert that one truck is used
        assertEquals(1, resultTrucks.size());
        assertFalse(resultTrucks.get(0).isEmpty());

    }

    @Test
    void testStackBoxesExceedingTruckCapacity() {
        // Create multiple boxes
        Box smallBox1 = new Box("SmallBox1", "X\nX", 'X'); // 1x2
        Box smallBox2 = new Box("SmallBox2", "X\nX", 'X'); // 1x2
        List<Box> boxList = List.of(smallBox1, smallBox2);

        // Stack them into one truck
        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(truckExample, boxList, 1);

        // Assert the number of trucks used
        assertEquals(1, resultTrucks.size());
        assertFalse(resultTrucks.get(0).isEmpty());
    }

    @Test
    void testStackBoxesWithLimitedTruckNumber() {
        Box box = new Box("Box1", "X\nX\nX", 'X');  // 1x3 Box
        List<Box> boxList = List.of(box);

        // Use a limited number of trucks
        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(truckExample, boxList, 1);

        // Expecting the box to fit into one truck
        assertEquals(1, resultTrucks.size());
        assertFalse(resultTrucks.get(0).isEmpty());
    }

    @Test
    void testStackBoxes_WithValidBoxes_ShouldNotThrow() {
        Box box1 = new Box("Box1", "###", '#'); // Fits in truck
        Box box2 = new Box("Box2", "##", '#');  // Fits in truck
        List<Box> boxes = List.of(box1, box2);

        Truck truck = new Truck(5, 3); // Enough room
        List<Truck> trucks = List.of(truck);

        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(trucks, boxes);

        // Validate the truck contains both boxes
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck())
                .containsEntry(box1, 1)
                .containsEntry(box2, 1);
    }

    // Test that throws exception when box cannot fit
    @Test
    void testStackBoxes_WhenBoxTooLarge_ShouldThrowException() {
        Truck smallTruck = new Truck(1, 1); // Too small
        List<Truck> trucks = List.of(smallTruck);

        Box box = new Box("Box1", "###", '#'); // Too large for the truck
        List<Box> boxes = List.of(box);

        assertThatThrownBy(() -> burkeBoxStackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class)
                .hasMessageContaining("Cannot stack box in truck");
    }

    // Test with a variety of boxes
    @Test
    void testStackBoxes_WithVariousSizedBoxes() {
        Truck truck = new Truck(10, 5);
        List<Truck> trucks = List.of(truck);

        Box box1 = new Box("Box1", "##", '#'); // Fits
        Box box2 = new Box("Box2", "###", '#'); // Fits too
        List<Box> boxes = List.of(box1, box2);

        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(trucks, boxes);

        // Verify the truck contains both boxes
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck())
                .containsEntry(box1, 1)
                .containsEntry(box2, 1);
    }

    // Test that throws exception when trying to stack with zero trucks
    @Test
    void testStackBoxes_WithZeroTrucks_ShouldThrowException() {
        List<Box> boxes = List.of(new Box("Box1", "#", '#')); // A box to stack

        assertThatThrownBy(() -> burkeBoxStackerStrategy.stackBoxes(new ArrayList<>(), boxes))
                .isInstanceOf(RuntimeException.class);
    }

    // Test stacks boxes when one truck is partially full
    @Test
    void testStackBoxes_WithPartiallyFullTruck() {
        Truck truck = new Truck(5, 5);
        truck.addBoxByCoordinates(0, 0, new Box("Box1", "#", '#')); // Adding box manually
        List<Truck> trucks = List.of(truck);

        Box box2 = new Box("Box2", "###", '#'); // Fits
        List<Box> boxes = List.of(box2);

        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(trucks, boxes);

        // Checking if box2 is added to the truck
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck())
                .containsEntry(box2, 1);
    }

    // Test for stacking when all boxes are too large
    @Test
    void testStackBoxes_WhenAllBoxesTooLarge_ShouldThrowException() {
        Truck truck = new Truck(2, 2); // Too small for the boxes
        List<Truck> trucks = List.of(truck);

        Box oversizedBox1 = new Box("Oversized1", "####", '#'); // Too large
        Box oversizedBox2 = new Box("Oversized2", "####", '#'); // Also too large
        List<Box> boxes = List.of(oversizedBox1, oversizedBox2);

        assertThatThrownBy(() -> burkeBoxStackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class)
                .hasMessageContaining("Cannot stack box in truck");
    }

    // Test for successfully stacking several small boxes
    @Test
    void testStackBoxes_WithMultipleSmallBoxes_Success() {
        Truck truck = new Truck(4, 4); // Enough space
        List<Truck> trucks = List.of(truck);

        Box smallBox1 = new Box("Small1", "#", '#'); // 1x1
        Box smallBox2 = new Box("Small2", "#", '#'); // 1x1
        List<Box> boxes = List.of(smallBox1, smallBox2);

        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(trucks, boxes);

        // Validate both boxes are in the truck
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck())
                .containsEntry(smallBox1, 1)
                .containsEntry(smallBox2, 1);
    }

    // Test that exceptions are thrown for incompatible box types
    @Test
    void testStackBoxes_WithIncompatibleBoxTypes_ShouldThrowException() {
        Truck truck = new Truck(5, 5);
        List<Truck> trucks = List.of(truck);

        Box box1 = new Box("Box1", "###", 'X'); // One type
        Box incompatibleBox = new Box("Box2", "######", 'Y'); // Another type
        List<Box> boxes = List.of(box1, incompatibleBox);

        assertThatThrownBy(() -> burkeBoxStackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class);
    }

    // Test that the strategy behaves when no boxes are provided
    @Test
    void testStackBoxes_EmptyBoxList_ShouldNotThrow() {
        Truck truck = new Truck(5, 5);
        List<Truck> trucks = List.of(truck);

        List<Truck> resultTrucks = burkeBoxStackerStrategy.stackBoxes(trucks, Collections.emptyList());

        // Validate the truck is empty, no errors should occur
        assertThat(resultTrucks).hasSize(0);
    }
}

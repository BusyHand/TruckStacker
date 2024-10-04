package ru.liga.truckstacker.service.stacker.impl.dummy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.exception.BoxCantStackInTruckException;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.when;


@SpringBootTest
public class DummyBoxStackerStrategyTest {
    @Autowired
    private DummyBoxStackerStrategy stackerStrategy;

    @Test
    void testStackBoxes_WithAvailableTrucks_Success() {
        // Given
        Box box1 = new Box("Box1", "#\n#\n#", '#');
        Box box2 = new Box("Box2", "##\n##", '#');
        List<Box> boxList = new ArrayList<>();
        boxList.add(box1);
        boxList.add(box2);

        // Create trucks for the boxes
        List<Truck> trucks = new ArrayList<>();
        Truck truck1 = new Truck(3, 2); // Enough space for Box1
        Truck truck2 = new Truck(3, 2); // Enough space for Box2
        trucks.add(truck1);
        trucks.add(truck2);

        // When
        List<Truck> resultTrucks = stackerStrategy.stackBoxes(trucks, boxList);

        // Then
        assertThat(resultTrucks).hasSize(2) // Both trucks should be used
                .extracting(truck -> truck.getBoxesInTruck()) // Extracting the boxes in each truck
                .satisfiesExactly(
                        boxesInTruck1 -> {
                            assertThat(boxesInTruck1).containsKey(box1); // Verify box1 in truck1
                            assertThat(boxesInTruck1.get(box1)).isEqualTo(1); // Quantity of box1
                        },
                        boxesInTruck2 -> {
                            assertThat(boxesInTruck2).containsKey(box2); // Verify box2 in truck2
                            assertThat(boxesInTruck2.get(box2)).isEqualTo(1); // Quantity of box2
                        }
                );
    }

    @Test
    void testStackBoxes_WithInsufficientTrucks() {
        // Given
        Box box1 = new Box("Box1", "#\n#\n#", '#');
        List<Box> boxList = new ArrayList<>();
        boxList.add(box1);

        // Create a truck that cannot fit the box
        List<Truck> trucks = new ArrayList<>();
        Truck smallTruck = new Truck(1, 1); // Too small for the box
        trucks.add(smallTruck);

        // When / Then
        assertThatExceptionOfType(BoxCantStackInTruckException.class)
                .isThrownBy(() -> stackerStrategy.stackBoxes(trucks, boxList));
    }


    @Test
    void testStackBoxes_ThrowsException_WhenBoxesExceedTrucks() {
        // Arrange
        Box box = new Box("Box1", "#", '#');
        List<Box> boxes = Arrays.asList(box);
        int maxTruckNumber = 0;

        // Act / Assert
        assertThatThrownBy(() -> stackerStrategy.stackBoxes(boxes, maxTruckNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("greater than the number of trucks");
    }


    // Test that stacks boxes correctly
    @Test
    void testStackBoxes_StacksBoxesCorrectly() {
        // Arrange
        Truck truck1 = new Truck(3, 3);
        Truck truck2 = new Truck(3, 3);
        List<Truck> trucks = new ArrayList<>();
        trucks.add(truck1);
        trucks.add(truck2);

        Box box1 = new Box("Box1", "##", '#'); // Assumes this box can fit
        Box box2 = new Box("Box2", "##", '#'); // Assumes this box can fit
        List<Box> boxes = new ArrayList<>();
        boxes.add(box1);
        boxes.add(box2);

        // Act
        List<Truck> resultTrucks = stackerStrategy.stackBoxes(trucks, boxes);

        // Assert
        assertThat(resultTrucks).hasSize(2); // Expecting both trucks to be used
        assertThat(resultTrucks)
                .extracting(Truck::getBoxesInTruck) // Extracting the boxes in each truck
                .satisfiesExactly(
                        boxesInTruck1 -> assertThat(boxesInTruck1).containsEntry(box1, 1), // Verify box1 in truck1 with quantity
                        boxesInTruck2 -> assertThat(boxesInTruck2).containsEntry(box2, 1)  // Verify box2 in truck2 with quantity
                );
    }

    // Test that throws an exception when no box fits
    @Test
    void testStackBoxes_ThrowsException_WhenNoBoxFits() {
        // Arrange
        Truck smallTruck = new Truck(1, 1); // Too small for the boxes
        List<Truck> trucks = new ArrayList<>();
        trucks.add(smallTruck);

        Box box = new Box("Box1", "###", '#'); // Too big for the truck
        List<Box> boxes = new ArrayList<>();
        boxes.add(box);

        // Act / Assert
        assertThatThrownBy(() -> stackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class)
                .hasMessage("Cannot stack box in truck");
    }

    // Test that stacks a single box successfully
    @Test
    void testStackBoxes_Success_WhenBoxesFit() {
        // Arrange
        Truck truck = new Truck(5, 5);
        List<Truck> trucks = new ArrayList<>();
        trucks.add(truck);

        Box box1 = new Box("Box1", "###", '#'); // Fits in the truck
        List<Box> boxes = new ArrayList<>();
        boxes.add(box1);

        // Act
        List<Truck> resultTrucks = stackerStrategy.stackBoxes(trucks, boxes);

        // Assert
        assertThat(resultTrucks).hasSize(1); // Expecting one truck to be used
        assertThat(resultTrucks)
                .extracting(Truck::getBoxesInTruck) // Extracting the boxes in each truck
                .satisfiesExactly(
                        boxesInTruck1 -> assertThat(boxesInTruck1).containsEntry(box1, 1) // Verify box1 in truck with quantity
                );
    }

    // Test that stacks a single box when multiple trucks are available
    @Test
    void testStackBoxes_SingleBoxWithMultipleTrucks() {
        // Arrange
        Truck truck1 = new Truck(5, 5);
        Truck truck2 = new Truck(5, 5); // Another truck available
        List<Truck> trucks = new ArrayList<>();
        trucks.add(truck1);
        trucks.add(truck2);

        Box box1 = new Box("Box1", "###", '#'); // Fits in truck1
        List<Box> boxes = new ArrayList<>();
        boxes.add(box1);

        // Act
        List<Truck> resultTrucks = stackerStrategy.stackBoxes(trucks, boxes);

        // Assert
        assertThat(resultTrucks).hasSize(1); // Only one truck should be used
        assertThat(resultTrucks)
                .extracting(Truck::getBoxesInTruck)
                .satisfiesExactly(
                        boxesInTruck1 -> assertThat(boxesInTruck1).containsEntry(box1, 1) // Verify box1 in the only used truck
                );
    }

    // Test with insufficient truck height for a box
    @Test
    void testStackBoxes_ThrowsException_WhenInsufficientHeight() {
        // Arrange
        Truck smallTruck = new Truck(2, 5); // Enough width but insufficient height for the box
        List<Truck> trucks = new ArrayList<>();
        trucks.add(smallTruck);

        Box box = new Box("Box1", "###\n###\n###", '#'); // Too high for the truck
        List<Box> boxes = new ArrayList<>();
        boxes.add(box);

        // Act / Assert
        assertThatThrownBy(() -> stackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class)
                .hasMessageContaining("Cannot stack box in truck"); // Adjust this based on the actual exception message
    }

    // Test with boxes that exceed the capacity of all trucks
    @Test
    void testStackBoxes_ThrowsException_WhenAllTrucksInsufficient() {
        // Arrange
        List<Truck> trucks = new ArrayList<>();
        trucks.add(new Truck(1, 1)); // All too small
        trucks.add(new Truck(1, 1));

        Box box1 = new Box("Box1", "###", '#'); // Too big for any truck
        List<Box> boxes = new ArrayList<>();
        boxes.add(box1);

        // Act / Assert
        assertThatThrownBy(() -> stackerStrategy.stackBoxes(trucks, boxes))
                .isInstanceOf(BoxCantStackInTruckException.class)
                .hasMessageContaining("Cannot stack box in truck");
    }
}

package ru.liga.truckstacker.service.stacker.impl.dummy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.convertor.BoxType;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class DummyBoxStackerServiceTest {


    @Autowired
    private DummyBoxStackerServiceImpl boxStackerService;

    @Test
    public void testStackBoxes_withValidData() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This truck should be empty.
        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);

        Box box = BoxType.ONE.getBox();
        List<Box> boxList = new ArrayList<>();
        boxList.add(box);

        // Execute the service
        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 1, boxList);

        // Assert that the truck contains the box
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).isEmpty()).isFalse();
    }

    @Test
    public void testStackBoxes_withMoreBoxesThanTrucks() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This truck should be empty.
        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);

        Box box = BoxType.ONE.getBox();
        List<Box> boxList = new ArrayList<>();
        // Add more boxes than available trucks
        boxList.add(box);
        boxList.add(box);

        // Expect an exception to be thrown
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            boxStackerService.stackBoxes(truckList, 1, boxList);
        });

        // Assert the exception message
        assertThat(thrown.getMessage()).isEqualTo("The number of boxes is greater than the number of trucks, you cannot use a Dummy algorithm.");
    }

    @Test
    public void testStackBoxes_withEmptyBoxList() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This truck should be empty.
        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);

        List<Box> boxList = new ArrayList<>(); // No boxes to stack

        // Execute the service
        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 1, boxList);

        // Assert that the truck remains empty
        assertThat(resultTrucks.isEmpty()).isTrue();
    }

    @Test
    public void testStackBoxes_withEmptyAndNonEmptyTrucks() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This should be empty
        Truck nonEmptyTruck = new Truck();
        nonEmptyTruck.getTruckArea()[0][0] = 1; // Simulate a box in the truck

        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);
        truckList.add(nonEmptyTruck); // Add a non-empty truck

        Box box = BoxType.ONE.getBox();
        List<Box> boxList = new ArrayList<>();
        boxList.add(box);

        // Execute the service
        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 2, boxList);

        // Assertion: The non-empty truck should still be non-empty, and the empty truck should contain the new box
        assertThat(resultTrucks).hasSize(2);
        assertThat(resultTrucks.get(0).isEmpty()).isFalse(); // Non-empty truck must not be empty
        assertThat(resultTrucks.get(1).isEmpty()).isFalse(); // The empty truck must be filled now
    }

    @Test
    public void testStackBoxes_withMoreBoxesThanTrucksIncludingNonEmpty() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This truck should be empty
        Truck nonEmptyTruck = new Truck();
        nonEmptyTruck.getTruckArea()[0][0] = 1; // Simulate a box in the truck

        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);
        truckList.add(nonEmptyTruck); // Add a non-empty truck

        Box box1 = BoxType.ONE.getBox();
        List<Box> boxList = new ArrayList<>();
        boxList.add(box1);
        boxList.add(box1); // Add more boxes than available trucks

        // Expect an exception to be thrown
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            boxStackerService.stackBoxes(truckList, 2, boxList);
        });

        // Assert the exception message
        assertThat(thrown.getMessage()).isEqualTo("The number of boxes is greater than the number of trucks, you cannot use a Dummy algorithm.");
    }

    @Test
    public void testStackBoxes_withEmptyBoxListAndNonEmptyTrucks() {
        // Prepare test data
        Truck emptyTruck = new Truck(); // This should be empty
        Truck nonEmptyTruck = new Truck();
        nonEmptyTruck.getTruckArea()[0][0] = 1; // Simulate a box in the truck

        List<Truck> truckList = new ArrayList<>();
        truckList.add(emptyTruck);
        truckList.add(nonEmptyTruck); // Add a non-empty truck

        List<Box> boxList = new ArrayList<>(); // No boxes to stack

        // Execute the service
        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 2, boxList);

        // Assert: The empty truck remains empty, the non-empty truck remains filled
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).isEmpty()).isFalse(); // The empty truck must remain empty
    }

    @Test
    public void testStackBoxes_withThreeNonEmptyTrucksAndOneEmptyTruck() {
        // Prepare test data
        Truck nonEmptyTruck1 = new Truck();
        Truck nonEmptyTruck2 = new Truck();
        Truck nonEmptyTruck3 = new Truck();

        // Simulate boxes in non-empty trucks
        nonEmptyTruck1.getTruckArea()[0][0] = 1; // Adding a box to Truck 1
        nonEmptyTruck2.getTruckArea()[0][1] = 1; // Adding a box to Truck 2
        nonEmptyTruck3.getTruckArea()[0][2] = 1; // Adding a box to Truck 3

        Truck emptyTruck = new Truck(); // This truck will be empty

        List<Truck> truckList = new ArrayList<>();
        truckList.add(nonEmptyTruck1);
        truckList.add(nonEmptyTruck2);
        truckList.add(nonEmptyTruck3);
        truckList.add(emptyTruck); // Add an empty truck

        Box box1 = BoxType.ONE.getBox();
        Box box2 = BoxType.ONE.getBox();
        List<Box> boxList = new ArrayList<>();
        boxList.add(box1);
        boxList.add(box2); // Add two boxes

        // Max truck number is 5
        int maxTruckNumber = 5;

        // Execute the service
        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, maxTruckNumber, boxList);

        // Assertions to verify the results
        assertThat(resultTrucks).hasSize(5); // Should have 4 trucks in total

        // Assert that non-empty trucks remain non-empty
        assertThat(resultTrucks.get(0).isEmpty()).isFalse(); // Non-empty Truck 1 should remain non-empty
        assertThat(resultTrucks.get(1).isEmpty()).isFalse(); // Non-empty Truck 2 should remain non-empty
        assertThat(resultTrucks.get(2).isEmpty()).isFalse(); // Non-empty Truck 3 should remain non-empty

        // Assert that the empty trucks is now filled with one of the boxes
        assertThat(resultTrucks.get(3).isEmpty()).isFalse(); // The empty truck must now hold a box
        assertThat(resultTrucks.get(4).isEmpty()).isFalse(); // The empty truck must now hold a box
    }


}

package ru.liga.truckstacker.service.stacker.strategy.burke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.liga.truckstacker.util.TruckBoxFiller.*;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class BurkeBoxStackerStrategyTest {

    @Autowired
    private BurkeBoxStackerStrategy boxStackerService;

    @Test
    public void testStackBoxes_withEmptyTrucks() {
        // Setup
        List<Truck> trucks = createEmptyTrucks(3);
        List<Box> boxes = createBoxes(5); // Create 5 boxes

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 3, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(3); // Should return 3 trucks
        assertThat(resultTrucks.get(0).isEmpty() &&
                resultTrucks.get(1).isEmpty() &&
                resultTrucks.get(2).isEmpty()).isFalse();

        // Additional checks can be added to verify box positions
    }

    @Test
    public void testStackBoxes_EmptyTrucks() {
        // Setup
        List<Truck> trucks = createFilledFullTrucks(1);
        List<Box> boxes = createBoxes(1);

        // Validate results
        assertThatThrownBy(() -> boxStackerService.stackBoxes(trucks, 1, boxes))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient trucks created");

        // Additional checks can be added to verify box positions
    }

    @Test
    public void testStackBoxes_withFullTrucks() {
        // Setup
        List<Truck> trucks = createFilledTrucks(3); // Create 3 filled trucks
        List<Box> boxes = createBoxes(2); // Creating 2 new boxes

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 3, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(3); // Should return 3 trucks
        // Here you can add assertions to confirm the filled boxes
    }


    @Test
    public void testStackBoxes_withSomeBoxTrucks() {
        // Setup
        List<Truck> trucks = createSomeBoxTrucks(3); // Trucks with some boxes filled
        List<Box> boxes = createBoxes(5); // Create 5 boxes

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 3, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(3); // Should return 3 trucks
        // Additional checks can be added for box placements
    }

    @Test
    public void testStackBoxes_WithMoreBoxesThanTrucks() {
        // Setup
        List<Truck> trucks = createEmptyTrucks(2); // Create 2 empty trucks
        List<Box> boxes = createBoxes(10); // Create 10 boxes

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 2, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(2); // Should return 2 trucks
        // Check that boxes are distributed across the trucks
        assertThat(resultTrucks.get(0).isEmpty()).isFalse();
        assertThat(resultTrucks.get(1).isEmpty()).isFalse();
    }

    @Test
    public void testStackBoxes_TryingToStackInFullTruck() {
        // Setup
        List<Truck> trucks = createFilledFullTrucks(1); // Create 1 full truck
        List<Box> boxes = createBoxes(1); // Create 1 box

        // Validate results
        assertThatThrownBy(() -> boxStackerService.stackBoxes(trucks, 1, boxes))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient trucks created");
    }

    @Test
    public void testStackBoxes_AllTrucksPartiallyFilled() {
        // Setup
        List<Truck> trucks = createSomeBoxTrucks(2); // Trucks with some boxes filled
        List<Box> boxes = createBoxes(6); // Create 6 boxes

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 2, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(2); // Should return 2 trucks
        // Additional checks can verify box positions if necessary
        assertThat(resultTrucks.get(0).isEmpty() &&
                resultTrucks.get(1).isEmpty()).isFalse();
    }

    @Test
    public void testStackBoxes_WithInsufficientTrucks() {
        // Setup
        List<Truck> trucks = createEmptyTrucks(1); // Create 1 empty truck
        List<Box> boxes = createBoxes(20); // Create 5 boxes

        // Validate results
        assertThatThrownBy(() -> boxStackerService.stackBoxes(trucks, 1, boxes))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient trucks created");
    }

    @Test
    public void testStackBoxes_FillingExactlyTheNumberOfTrucksNeeded() {
        // Setup
        List<Truck> trucks = createEmptyTrucks(2); // Create 2 empty trucks
        List<Box> boxes = createBoxes(8); // Assume 4 boxes per truck will fit

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 2, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(2); // Should return 2 trucks
        // Additional checks can be added to verify boxes are filled completely in each truck
    }

    @Test
    public void testStackBoxes_StackingIntoMultipleTrucks() {
        // Setup
        List<Truck> trucks = createEmptyTrucks(3);
        List<Box> boxes = createBoxes(20);

        // Execute
        List<Truck> resultTrucks = boxStackerService.stackBoxes(trucks, 3, boxes);

        // Validate results
        assertThat(resultTrucks).hasSize(3);
        // Validate that boxes have been stacked appropriately
        assertThat(resultTrucks.stream().anyMatch(Truck::isEmpty)).isFalse();
    }

}

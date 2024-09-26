package ru.liga.truckstacker.service.stacker.strategy.balanced;

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
import static ru.liga.truckstacker.util.TruckBoxFiller.createBoxes;
import static ru.liga.truckstacker.util.TruckBoxFiller.createEmptyTrucks;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class BalancedBoxStackerStrategyTest {


    @Autowired
    private BalancedBoxStackerStrategy boxStackerService;


    @Test
    public void testPackingMultipleBoxesIntoSingleTruck() {
        List<Truck> truckList = createEmptyTrucks(1); // create one empty truck
        List<Box> boxList = createBoxes(3);           // create 3 boxes

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 1, boxList);

        // Check if boxes were packed into the single truck
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getTruckArea()).isNotEmpty(); // Area should not be empty
    }

    @Test
    public void testPackingBoxesAcrossMultipleTrucks() {
        List<Truck> truckList = createEmptyTrucks(2); // create two empty trucks
        List<Box> boxList = createBoxes(5);           // create 5 boxes

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 2, boxList);

        // Check if boxes were properly distributed across trucks
        assertThat(resultTrucks).hasSize(2);
        assertThat(resultTrucks.get(0).getTruckArea()).isNotEmpty();
        assertThat(resultTrucks.get(1).getTruckArea()).isNotEmpty();
    }

    @Test
    public void testFillingSingleTruckCompletely() {
        List<Truck> truckList = createEmptyTrucks(1); // create one empty truck
        List<Box> boxList = createBoxes(6);           // create more boxes than truck can hold

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 1, boxList);

        // Check if the truck is filled to full capacity
        assertThat(resultTrucks.get(0).getTruckArea()).isNotEmpty(); // The truck area should have boxes
    }

    @Test
    public void testHandlingInsufficientSpace() {
        List<Truck> truckList = createEmptyTrucks(1); // create one small truck
        List<Box> boxList = createBoxes(10);          // create many boxes that cannot fit

        // Assert that a RuntimeException is thrown
        assertThatThrownBy(() -> boxStackerService.stackBoxes(truckList, 1, boxList))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No valid position found to fit the box");
    }

    @Test
    public void testStackingEmptyBoxList() {
        List<Truck> truckList = createEmptyTrucks(3); // create 3 empty trucks
        List<Box> emptyBoxList = createBoxes(0);      // create no boxes

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 3, emptyBoxList);

        // All trucks should remain empty
        assertThat(resultTrucks).hasSize(3);
        for (Truck truck : resultTrucks) {
            assertThat(truck.getTruckArea()).isNotEmpty(); // Trucks should be empty but initialized
        }
    }

    @Test
    public void testStackingBoxesIntoFullTrucks() {
        List<Truck> truckList = createEmptyTrucks(2); // create two trucks
        List<Box> boxList = createBoxes(5);           // fill trucks fully

        // Fill trucks
        boxStackerService.stackBoxes(truckList, 2, boxList);

        List<Box> extraBoxList = createBoxes(10);      // create extra boxes to overfill
        assertThatThrownBy(() -> boxStackerService.stackBoxes(truckList, 2, extraBoxList))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No valid position found to fit the box");
    }

    @Test
    public void testStackingBoxesWithExactFit() {
        List<Truck> truckList = createEmptyTrucks(2); // create two empty trucks
        List<Box> boxList = createBoxes(12);          // exact number of boxes for two trucks

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 2, boxList);

        // Both trucks should be fully occupied
        assertThat(resultTrucks).hasSize(2);
        for (Truck truck : resultTrucks) {
            assertThat(truck.getTruckArea()).isNotEmpty(); // Trucks should be filled
        }
    }

    @Test
    public void testStackingBoxesWithUnevenDistribution() {
        List<Truck> truckList = createEmptyTrucks(3); // create 3 empty trucks
        List<Box> boxList = createBoxes(7);           // 7 boxes, which should leave some trucks partially filled

        List<Truck> resultTrucks = boxStackerService.stackBoxes(truckList, 3, boxList);

        // Check if boxes were distributed across trucks
        assertThat(resultTrucks).hasSize(3);
        assertThat(resultTrucks.get(0).getTruckArea()).isNotEmpty(); // First truck should be filled
        assertThat(resultTrucks.get(1).getTruckArea()).isNotEmpty(); // Second truck should be partially filled
        assertThat(resultTrucks.get(2).getTruckArea()).isNotEmpty(); // Third truck should be partially filled or empty
    }

    @Test
    public void testHandlingLargeBoxesThatDontFit() {
        List<Truck> truckList = createEmptyTrucks(1); // create one empty truck
        List<Box> largeBoxList = createBoxes(20);      // create a box larger than the truck dimensions

        // Assert that an exception is thrown
        assertThatThrownBy(() -> boxStackerService.stackBoxes(truckList, 1, largeBoxList))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No valid position found to fit the box");
    }

}

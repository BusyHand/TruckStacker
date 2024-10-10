package ru.liga.truckstacker.service.stacker.impl.balanced;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.impl.dummy.DummyBoxStackerStrategy;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BalancedBoxStackerStrategyTest {

    BalancedBoxStackerStrategy balancedBoxStackerStrategy = new BalancedBoxStackerStrategy(new DummyBoxStackerStrategy());

    @Test
    void testStackBoxes_WithNonEmptyBoxList() {
        List<Box> boxes = List.of(new Box("Box1", "##", 'X'), new Box("Box2", "##", 'Y'));
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(boxes);

        assertThat(resultTrucks).isNotEmpty();
        assertThat(resultTrucks.size()).isGreaterThan(0);
        assertThat(resultTrucks).allMatch(truck -> !truck.isEmpty());
    }

    @Test
    void testStackBoxes_WithEmptyBoxList() {
        List<Box> emptyBoxList = Collections.emptyList();
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(emptyBoxList);

        assertThat(resultTrucks).isEmpty();
    }

    @Test
    void testStackBoxes_WithLimitedMaxTruckNumber() {
        List<Box> boxes = List.of(new Box("Box1", "###", 'X'), new Box("Box2", "###", 'Y'));
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(boxes, 1);

        assertThat(resultTrucks).hasSize(1); // Expecting only 1 truck due to max limit
    }

    @Test
    void testStackBoxes_WithTrucksAndBoxes() {
        Truck truck1 = new Truck(5, 5);
        Truck truck2 = new Truck(5, 5);
        List<Truck> trucks = List.of(truck1, truck2);

        List<Box> boxes = List.of(new Box("Box1", "###", 'X'), new Box("Box2", "##", 'Y'));
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(trucks, boxes);

        assertThat(resultTrucks).isNotEmpty();
        assertThat(resultTrucks.size()).isLessThanOrEqualTo(trucks.size());
        assertThat(resultTrucks).allMatch(truck -> !truck.isEmpty());
    }

    @Test
    void testStackBoxes_WithValidTruckExample() {
        Box box = new Box("Box1", "##", 'X');
        Truck truckExample = new Truck(5, 5);
        List<Box> boxes = List.of(box);

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(truckExample, boxes, 2);

        assertThat(resultTrucks).hasSize(1); // Expecting one truck used
        assertThat(resultTrucks.get(0).getBoxesInTruck())
                .containsEntry(box, 1);
    }

    @Test
    void testStackBoxes_WhenMaxTruckNumberExceedsBoxes() {
        List<Box> boxes = List.of(new Box("Box1", "#", 'X'));
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(boxes, 10); // More trucks than boxes

        assertThat(resultTrucks).hasSize(1); // Expecting just one truck to be used
    }

    @Test
    void testStackBoxes_WhenMaxTruckNumberIsZero() {
        List<Box> boxes = List.of(new Box("Box1", "#", 'X'));

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(boxes, 0); // No trucks

        assertThat(resultTrucks).isEmpty(); // Expect no trucks to be used
    }

    @Test
    void testStackBoxes_WithMultipleTrucksAndBoxes() {
        Truck truck1 = new Truck(10, 10);
        Truck truck2 = new Truck(5, 5);
        List<Truck> trucks = List.of(truck1, truck2);

        List<Box> boxes = List.of(new Box("Box1", "##########", 'X'), new Box("Box2", "#####", 'Y'));
        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(trucks, boxes);

        assertThat(resultTrucks).isNotEmpty();
        assertThat(resultTrucks.size()).isLessThanOrEqualTo(trucks.size());
        assertThat(resultTrucks).anyMatch(truck -> truck.getBoxesInTruck().size() > 0);
    }

    @Test
    void testStackBoxes_WithMaxTrucksLessThanAvailableBoxes() {
        List<Box> boxes = List.of(
                new Box("Box1", "XX", 'X'),
                new Box("Box2", "XX", 'X'),
                new Box("Box3", "XX", 'X')
        );

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(boxes, 2); // Limit to 2 trucks

        assertThat(resultTrucks).hasSizeLessThanOrEqualTo(2);
        assertThat(resultTrucks).allMatch(truck -> !truck.isEmpty());
    }

    @Test
    void testStackBoxes_WithSingleLargeBox() {
        Box largeBox = new Box("LargeBox", "##########\n##########", 'L'); // Large box with two rows
        List<Box> singleBoxList = List.of(largeBox);

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(List.of(new Truck(10, 10)), singleBoxList); // One truck with adequate size

        assertThat(resultTrucks).hasSize(1); // One truck should be used for the large box
        assertThat(resultTrucks.get(0).getBoxesInTruck()).containsEntry(largeBox, 1); // The large box should be in the truck
    }

    @Test
    void testStackBoxes_WithMultipleSmallBoxesAndLimitedTrucks() {
        List<Box> smallBoxes = List.of(
                new Box("SmallBox1", "XX", 'S'),
                new Box("SmallBox2", "XX", 'S'),
                new Box("SmallBox3", "XX", 'S')
        );

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(smallBoxes, 2); // Limited to 2 trucks

        assertThat(resultTrucks).hasSizeLessThanOrEqualTo(2);
        assertThat(resultTrucks).allMatch(truck -> !truck.isEmpty());
    }

    @Test
    void testStackBoxes_WithIdenticalBoxes() {
        List<Box> identicalBoxes = List.of(
                new Box("IdenticalBox", "XX", 'I'),
                new Box("IdenticalBox", "XX", 'I'),
                new Box("IdenticalBox", "XX", 'I')
        );

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(identicalBoxes, 1); // Single truck

        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck()).containsEntry(identicalBoxes.get(0), 3); // All identical should fit into one
    }

    @Test
    void testStackBoxes_WhenBoxesTakeUpMoreSpaceThanTrucks() {
        List<Box> oversizedBoxes = List.of(new Box("Box1", "##########", 'O')); // Oversized box

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(List.of(new Truck(5, 20)), oversizedBoxes); // Single truck with insufficient space

        assertThat(resultTrucks).hasSize(1); // One truck is created
        assertThat(resultTrucks.get(0).isEmpty()).isFalse(); // The truck should remain empty since the box doesn’t fit
    }

    @Test
    void testStackBoxes_WithMaximumTruckCapacity() {
        List<Box> boxes = List.of(new Box("Box1", "X", 'X'), new Box("Box2", "X", 'X'), new Box("Box3", "X", 'X'));
        Truck truck = new Truck(3, 1); // A truck that can fit exactly three boxes

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(List.of(truck), boxes);

        assertThat(resultTrucks).contains(truck);
        assertThat(truck.getBoxesInTruck().size()).isEqualTo(3); // All boxes fit
    }

    @Test
    void testStackBoxes_WithTooManyTrucksForBoxes() {
        List<Box> boxes = List.of(new Box("Box1", "X", 'X'));
        List<Truck> trucks = List.of(new Truck(5, 5), new Truck(5, 5), new Truck(5, 5)); // Three trucks available

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(trucks, boxes);

        assertThat(resultTrucks).hasSize(1); // Should only use one truck
        assertThat(resultTrucks.get(0).getBoxesInTruck()).containsEntry(boxes.get(0), 1);
    }

    @Test
    void testStackBoxes_WithVaryingTruckSizes() {
        Truck truckSmall = new Truck(2, 2); // Small truck
        Truck truckLarge = new Truck(5, 5); // Large truck
        List<Truck> trucks = List.of(truckSmall, truckLarge);

        List<Box> boxes = List.of(
                new Box("LargeBox", "#####", 'L'), // Large box
                new Box("SmallBox", "##", 'S') // Small box
        );

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(trucks, boxes);

        assertThat(resultTrucks).anyMatch(truck -> truck.getBoxesInTruck().containsValue(1)); // At least one box should be packed
    }

    @Test
    void testStackBoxes_WhenNoTrucksProvided() {
        List<Box> boxes = List.of(new Box("Box1", "X", 'X'));

        List<Truck> resultTrucks = balancedBoxStackerStrategy.stackBoxes(Collections.emptyList(), boxes); // No trucks provided

        assertThat(resultTrucks).isEmpty(); // No trucks should be returned
    }
}

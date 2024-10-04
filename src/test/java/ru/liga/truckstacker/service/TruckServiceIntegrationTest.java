package ru.liga.truckstacker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TruckServiceIntegrationTest {

    @Autowired
    private TruckService truckService;

    @Autowired
    private BoxService boxService;

    @BeforeEach
    public void before() throws IOException {
        Path pathDb = Path.of("db", "boxes", "boxes.txt");
        if (Files.exists(pathDb)) {
            Files.delete(pathDb);
        }
    }

    @Test
    void testStackBoxesAndSaveTrucks_NoTrucks() {
        StackingRequest request = createStackingRequest(5, "BALANCED", "", "", "", "");
        fillBoxes(List.of(new Box("Box1", "##", 'X')));
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).isEmpty();
    }

    @Test
    void testStackBoxesAndSaveTrucks_OneTruck() {
        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);
        StackingRequest request = createStackingRequest(1, "BALANCED", "", "", "Box1", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck()).containsKey(box);
    }

    @Test
    void testStackBoxesAndSaveTrucks_MultipleTrucks() {
        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);
        StackingRequest request = createStackingRequest(2, "BALANCED", "", "", "Box1,Box1", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(2);
        for (Truck truck : resultTrucks) {
            assertThat(truck.getBoxesInTruck()).containsKey(box);
        }
    }

    @Test
    void testStackBoxesWithTruckSize() {
        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);
        StackingRequest request = createStackingRequest(1, "BALANCED", "", "7x3", "Box1", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getHeight()).isEqualTo(7);
        assertThat(resultTrucks.get(0).getWidth()).isEqualTo(3);
    }

    @Test
    void testStackBoxesWithTruckSize3() {
        Box box1 = new Box("Box1", "##", 'X');
        Box box2 = new Box("Box2", "# \n##", 'X');
        boxService.createBox(box1);
        boxService.createBox(box2);
        StackingRequest request = createStackingRequest(2, "dummy", "", "7x3, 2x3", "Box1,Box2", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(2);
        assertThat(resultTrucks.get(0).getHeight()).isEqualTo(2);
        assertThat(resultTrucks.get(0).getWidth()).isEqualTo(3);
        assertThat(resultTrucks.get(1).getHeight()).isEqualTo(7);
        assertThat(resultTrucks.get(1).getWidth()).isEqualTo(3);
    }

    @Test
    void testStackBoxesWithTruckSize5() {
        Box box1 = new Box("Box1", "##", 'X');
        Box box2 = new Box("Box2", "##", 'X');
        boxService.createBox(box1);
        boxService.createBox(box2);
        StackingRequest request = createStackingRequest(1, "balanced", "", "7x3", "Box1,Box2", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getHeight()).isEqualTo(7);
        assertThat(resultTrucks.get(0).getWidth()).isEqualTo(3);
    }

    @Test
    void testStackBoxesWithTruckSize6() {
        Box box1 = new Box("Box1", "##", 'X');
        Box box2 = new Box("Box2", "# \n##\n", 'X');
        boxService.createBox(box1);
        boxService.createBox(box2);
        StackingRequest request = createStackingRequest(2, "burke", "", "7x3, 2x3", "Box1,Box2", "");
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getHeight()).isEqualTo(7);
        assertThat(resultTrucks.get(0).getWidth()).isEqualTo(3);
    }

    @Test
    void testStackBoxesWithEmptyFileNames() {
        StackingRequest request = createStackingRequest(1, "BALANCED", "", "", "", ""); // All file names empty
        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);
        assertThat(resultTrucks).isEmpty(); // Adjust expected results based on your logic
    }

    // Helper method to create StackingRequest
    private StackingRequest createStackingRequest(int limit, String algorithm, String boxesFile, String truckSize, String boxNames, String alreadyFilledTrucks) {
        return StackingRequest.builder()
                .limitNumberOfTrucks(limit)
                .typeAlgorithmName(algorithm)
                .boxesFileName(boxesFile)
                .truckSize(truckSize)
                .boxNames(boxNames)
                .alreadyFilledTrucksFileName(alreadyFilledTrucks)
                .build();
    }

    // Helper method to fill boxes
    private void fillBoxes(List<Box> boxes) {
        for (Box box : boxes) {
            boxService.createBox(box);
        }
    }

    @Test
    void testStackBoxesAndSaveTrucks_NoTrucks2() {
        StackingRequest request = StackingRequest.builder()
                .limitNumberOfTrucks(5)
                .typeAlgorithmName("BALANCED")
                .boxesFileName("")
                .truckSize("")
                .boxNames("")
                .alreadyFilledTrucksFileName("")
                .build();

        // Fill boxes here if necessary
        List<Box> boxes = List.of(new Box("Box1", "##", 'X'));
        for (Box box : boxes) {
            boxService.createBox(box);
        }

        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);

        assertThat(resultTrucks).isEmpty();
    }

    @Test
    void testStackBoxesAndSaveTrucks_OneTruck2() {


        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);

        StackingRequest request = StackingRequest.builder()
                .limitNumberOfTrucks(1)
                .typeAlgorithmName("BALANCED")
                .boxesFileName("")
                .truckSize("")
                .boxNames("Box1")
                .alreadyFilledTrucksFileName("")
                .build();

        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);

        assertThat(resultTrucks).hasSize(1);
        assertThat(resultTrucks.get(0).getBoxesInTruck()).containsKey(box);
    }

    @Test
    void testStackBoxesAndSaveTrucks_MultipleTrucks4() {

        Box box = new Box("Box1", "##", 'X');
        boxService.createBox(box);

        StackingRequest request = StackingRequest.builder()
                .limitNumberOfTrucks(2)
                .typeAlgorithmName("BALANCED")
                .boxesFileName("")
                .truckSize("")
                .boxNames("Box1,Box1")
                .alreadyFilledTrucksFileName("")
                .build();

        List<Truck> resultTrucks = truckService.stackBoxesAndSaveTrucks(request);

        assertThat(resultTrucks).hasSize(2);
        for (Truck truck : resultTrucks) {
            assertThat(truck.getBoxesInTruck()).containsKey(box);
        }
    }
}

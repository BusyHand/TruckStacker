package ru.liga.truckstacker.service.stacker.impl.burke.logic;

import ru.liga.truckstacker.service.stacker.impl.burke.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;
import ru.liga.truckstacker.service.stacker.impl.burke.validation.CellsValid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurkeAlgorithm {

    private final CoordinatesFinder coordinatesFinder = new CoordinatesFinder();
    private final BoxFitter boxFitter = new BoxFitter();
    private final HeightAdjuster heightAdjuster = new HeightAdjuster();

    private final CellsValid cellsValid = new CellsValid();


    public List<HeightStatTruck> stackBox(List<HeightStatBox> heightStatBoxes) {
        List<HeightStatTruck> trucks = new ArrayList<>();
        HeightStatTruck currentTruck = new HeightStatTruck();
        trucks.add(currentTruck);
        while (!heightStatBoxes.isEmpty()) {
            final int UNLIMITED_SIZE = trucks.size() + 1;
            currentTruck = checkAndAddNewTruck(UNLIMITED_SIZE, trucks, currentTruck);
            doBurkeAlgorithm(heightStatBoxes, currentTruck);
        }

        return trucks;
    }

    public List<HeightStatTruck> stackBoxInLimitedTrucks(List<HeightStatTruck> existingTrucks, int maxSizeNumberTrucks, List<HeightStatBox> heightStatBoxes) {
        List<HeightStatTruck> trucks = new ArrayList<>(existingTrucks);
        final int FIRST_TRUCK = 0;
        HeightStatTruck currentTruck = trucks.isEmpty() ? new HeightStatTruck() : trucks.get(FIRST_TRUCK);
        while (!heightStatBoxes.isEmpty()) {
            currentTruck = checkAndAddNewTruck(maxSizeNumberTrucks, trucks, currentTruck);
            doBurkeAlgorithm(heightStatBoxes, currentTruck);
        }
        return trucks;
    }


    private void doBurkeAlgorithm(List<HeightStatBox> heightStatBoxes, HeightStatTruck currentTruck) {
        CoordinatesInTruckArea coordinates = coordinatesFinder.findMinHeightCoordinates(currentTruck);
        heightStatBoxes.stream()
                .filter(box -> cellsValid.canFit(coordinates, box, currentTruck))
                .findFirst()
                .ifPresentOrElse(box -> {
                            boxFitter.fitBoxByCoordinates(coordinates, box, currentTruck);
                            heightAdjuster.raiseHeightWhereBoxStacked(coordinates, box, currentTruck);
                            heightStatBoxes.remove(box);
                        },
                        () -> heightAdjuster.raiseHeightToLowestNeighbour(coordinates, currentTruck));
    }

    private HeightStatTruck checkAndAddNewTruck(int maxSizeNumberTrucks, List<HeightStatTruck> trucks, HeightStatTruck currentTruck) {
        if (currentTruck.isFull()) {
            Optional<HeightStatTruck> truck = findTruckWithSpace(trucks);
            currentTruck = truck.orElseGet(() -> {
                if (trucks.size() >= maxSizeNumberTrucks) {
                    throw new RuntimeException("Insufficient trucks created. Required more than: " + maxSizeNumberTrucks + ", Actual: " + trucks.size());
                }
                HeightStatTruck newTruck = new HeightStatTruck();
                trucks.add(newTruck);
                return newTruck;
            });
        }
        return currentTruck;
    }

    private Optional<HeightStatTruck> findTruckWithSpace(List<HeightStatTruck> trucks) {
        return trucks.stream()
                .filter(truck -> !truck.isFull())
                .findFirst();
    }
}

package ru.liga.truckstacker.service.stacker.strategy.burke.logic;

import ru.liga.truckstacker.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatTruck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Алгоритм Бёрка для упаковки грузовиков.
 * Этот класс управляет укладкой коробок в грузовики с учетом высотных характеристик,
 * и обеспечивает оптимальное распределение грузов по доступным грузовикам.
 */
public class BurkeAlgorithm {

    private final HeightAdjuster heightAdjuster = new HeightAdjuster();

    /**
     * Укладывает коробки в грузовики на основе заданного списка коробок без ограничения по количеству грузовиков.
     * @param heightStatBoxes Список коробок для укладки в грузовики.
     * @return Список грузовиков с укладкой коробок.
     */
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

    /**
     * Укладывает коробки в существующие грузовики с ограничением на максимальное количество грузовиков.
     * @param existingTrucks Существующий список грузовиков.
     * @param maxSizeNumberTrucks Максимальное количество грузовиков, которое можно использовать.
     * @param heightStatBoxes Список коробок для укладки.
     * @return Список грузовиков с укладкой коробок.
     */
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
        CoordinatesInTruckArea coordinates = findMinHeightCoordinates(currentTruck);
        heightStatBoxes.stream()
                .filter(box -> currentTruck.getTruck().canFit(coordinates, box.getBox()))
                .findFirst()
                .ifPresentOrElse(box -> addBoxToTruck(box, coordinates, currentTruck, heightStatBoxes),
                        () -> handleNoBoxFit(coordinates, currentTruck));
    }

    private void addBoxToTruck(HeightStatBox box, CoordinatesInTruckArea coordinates, HeightStatTruck currentTruck, List<HeightStatBox> heightStatBoxes) {
        Truck truck = currentTruck.getTruck();
        truck.addBoxByCoordinates(coordinates, box.getBox());
        heightAdjuster.raiseHeightWhereBoxStacked(coordinates, box, currentTruck);
        heightStatBoxes.remove(box);
    }

    private void handleNoBoxFit(CoordinatesInTruckArea coordinates, HeightStatTruck currentTruck) {
        heightAdjuster.raiseHeightToLowestNeighbour(coordinates, currentTruck);
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

    private CoordinatesInTruckArea findMinHeightCoordinates(HeightStatTruck heightStatTruck) {
        int[] heightStat = heightStatTruck.getHeightStat();
        int x = 0;
        int y = Integer.MAX_VALUE;

        for (int i = 0; i < heightStat.length; i++) {
            if (y > heightStat[i]) {
                x = i;
                y = heightStat[i];
            }
        }
        return new CoordinatesInTruckArea(x, y);
    }
}

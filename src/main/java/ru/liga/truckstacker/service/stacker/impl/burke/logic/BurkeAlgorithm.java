package ru.liga.truckstacker.service.stacker.impl.burke.logic;

import ru.liga.truckstacker.exception.BoxCantStackInTruckException;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;
import ru.liga.truckstacker.util.CoordinatesInTruckArea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Алгоритм Бёрка для упаковки грузовиков.
 * Этот класс управляет укладкой коробок в грузовики с учетом высотных характеристик,
 * и обеспечивает оптимальное распределение грузов по доступным грузовикам.
 */
public class BurkeAlgorithm {

    /**
     * Укладывает коробки в грузовики на основе заданного списка коробок без ограничения по количеству грузовиков.
     *
     * @param heightStatBoxes Список коробок для укладки в грузовики.
     * @return Список грузовиков с укладкой коробок.
     */
    public List<HeightStatTruck> stackBox(List<HeightStatBox> heightStatBoxes) {
        List<HeightStatTruck> trucks = new ArrayList<>();
        HeightStatTruck currentTruck = new HeightStatTruck();
        trucks.add(currentTruck);
        while (!heightStatBoxes.isEmpty()) {
            final int UNLIMITED_SIZE = trucks.size() + 1;
            currentTruck = checkAndAddNewTruck(trucks, currentTruck);
            doBurkeAlgorithm(heightStatBoxes, currentTruck);
        }

        return trucks;
    }

    /**
     * Укладывает коробки в существующие грузовики с ограничением на максимальное количество грузовиков.
     *
     * @param trucks              Существующий список грузовиков.
     * @param maxSizeNumberTrucks Максимальное количество грузовиков, которое можно использовать.
     * @param heightStatBoxes     Список коробок для укладки.
     * @return Список грузовиков с укладкой коробок.
     */
    public List<HeightStatTruck> stackBoxInLimitedTrucks(List<HeightStatTruck> trucks, int maxSizeNumberTrucks, List<HeightStatBox> heightStatBoxes) {
        HeightStatTruck currentTruck = new HeightStatTruck();
        trucks.add(currentTruck);

        while (!heightStatBoxes.isEmpty()) {
            currentTruck = checkAndAddNewTruck(trucks, currentTruck);
            if (trucks.size() > maxSizeNumberTrucks) {
                throw new RuntimeException("Insufficient trucks created. Required more than: " + maxSizeNumberTrucks + ", Actual: " + trucks.size());
            }
            doBurkeAlgorithm(heightStatBoxes, currentTruck);
        }
        return trucks;
    }

    public List<HeightStatTruck> stackBoxInLimitedTrucks(List<HeightStatTruck> existingTrucks, List<HeightStatBox> heightStatBoxes) {
        List<HeightStatTruck> trucks = new ArrayList<>(existingTrucks);
        Iterator<HeightStatTruck> iterator = trucks.iterator();
        if (!iterator.hasNext()) {
            throw new BoxCantStackInTruckException("Cannot stack box in truck");
        }
        HeightStatTruck currentTruck = iterator.next();

        while (!heightStatBoxes.isEmpty()) {
            if (currentTruck.isFull()) {
                if (!iterator.hasNext()) {
                    throw new BoxCantStackInTruckException("Cannot stack box in truck");
                }
                currentTruck = iterator.next();
            }
            doBurkeAlgorithm(heightStatBoxes, currentTruck);
        }
        return trucks;
    }


    private void doBurkeAlgorithm(List<HeightStatBox> heightStatBoxes, HeightStatTruck currentTruck) {
        CoordinatesInTruckArea coordinates = currentTruck.findMinHeightCoordinates();
        heightStatBoxes.stream()
                .filter(heightStatBox -> currentTruck.getTruck().canFit(coordinates, heightStatBox.getBox()))
                .findFirst()
                .ifPresentOrElse(box -> addBoxToTruck(box, coordinates, currentTruck, heightStatBoxes),
                        () -> handleNoBoxFit(coordinates, currentTruck));
    }

    private void addBoxToTruck(HeightStatBox box, CoordinatesInTruckArea coordinates, HeightStatTruck currentTruck, List<HeightStatBox> heightStatBoxes) {
        Truck truck = currentTruck.getTruck();
        truck.addBoxByCoordinates(coordinates, box.getBox());
        currentTruck.raiseHeightWhereBoxStacked(coordinates, box);
        heightStatBoxes.remove(box);
    }

    private void handleNoBoxFit(CoordinatesInTruckArea coordinates, HeightStatTruck currentTruck) {
        currentTruck.raiseHeightToLowestNeighbour(coordinates);
    }

    private HeightStatTruck checkAndAddNewTruck(List<HeightStatTruck> trucks, HeightStatTruck currentTruck) {
        if (currentTruck.isFull()) {
            currentTruck = new HeightStatTruck();
            trucks.add(currentTruck);
        }
        return currentTruck;
    }
}

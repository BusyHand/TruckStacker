package ru.liga.truckstacker.service.stacker.impl.balanced.logic;

import lombok.extern.slf4j.Slf4j;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.record.CoordinatesInTruckArea;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatTruck;

import java.util.Deque;
import java.util.PriorityQueue;

/**
 * Сбалансированный алгоритм упаковки, который равномерно распределяет коробки в грузовики
 */
@Slf4j
public class BalancedAlgorithm {
    /**
     * Реализует алгоритм сбалансированной укладки коробок в грузовики.
     * <p>
     * Идея заключается в том, чтобы создать обертку над обычным грузовиком,
     * которая будет считать количество свободного места в грузовике.
     * Алгоритм использует приоритетную очередь, которая извлекает грузовик
     * с самой большой свободной площадью, и сортирует все коробки по
     * убыванию их площадей.
     * <p>
     * Шаги алгоритма:
     * 1) Достаем грузовик с самым большим свободным местом из приоритетной очереди;
     * 2) Достаем коробку с самой большой площадью;
     * 3) Укладываем коробку в грузовик (производится подсчет свободного места в грузовике);
     * 4) Кладем грузовик обратно (очевидно с меньшей свободной площадью);
     * 5) И так продолжаем, пока не закончатся все коробки.
     *
     * @param truckPriorityQueue Приоритетная очередь грузовиков, доступных для укладки;
     * @param areaStatBoxDeque   Дек для коробок, упорядоченных по убыванию их площадей.
     * @return PriorityQueue<AreaStatTruck> Обновленная приоритетная очередь грузовиков с учетом добавленных коробок.
     */
    public PriorityQueue<AreaStatTruck> doBalancedAlgorithm(PriorityQueue<AreaStatTruck> truckPriorityQueue, Deque<AreaStatBox> areaStatBoxDeque) {
        log.info("Starting balanced stacking algorithm.");
        while (!areaStatBoxDeque.isEmpty() && !truckPriorityQueue.isEmpty()) {
            AreaStatBox box = areaStatBoxDeque.poll();
            AreaStatTruck currentTruck = truckPriorityQueue.poll();

            log.debug("Processing box with area: {}. Current truck free area: {}", box.getArea(), currentTruck.getFreeArea());

            CoordinatesInTruckArea coordinates = findPlaceToFit(currentTruck, box);
            currentTruck.getTruck().addBoxByCoordinates(coordinates, box.getBox());

            currentTruck.setFreeArea(currentTruck.getFreeArea() - box.getArea());
            log.debug("Box fitted in truck. Updated free area: {}", currentTruck.getFreeArea());

            truckPriorityQueue.add(currentTruck);
        }

        log.info("Balancing algorithm finished. Returning results.");
        return truckPriorityQueue;
    }

    private CoordinatesInTruckArea findPlaceToFit(AreaStatTruck currentTruck, AreaStatBox areaStatBox) {
        Truck truck = currentTruck.getTruck();
        Box box = areaStatBox.getBox();
        int truckHeight = truck.getHeight();
        int truckWidth = truck.getWidth();
        int boxHeight = box.getHeight();
        int boxWidth = box.getWidth();

        for (int y = 0; y <= truckHeight - boxHeight; y++) {
            for (int x = 0; x <= truckWidth - boxWidth; x++) {
                if (truck.canFit(x, y, box)) {
                    return new CoordinatesInTruckArea(x, y);
                }
            }
        }
        throw new RuntimeException("No valid position found to fit the box '" + box.getForm() +
                "' in the largest available truck with area " + currentTruck.getFreeArea() + ".");
    }
}

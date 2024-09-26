
package ru.liga.truckstacker.service.stacker;

import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.List;
/**
 * Интерфейс для стратегий укладки коробок, определяющий методы для укладки.
 */
public interface BoxStackerStrategy {
    /**
     * Укладывает коробки без учета грузовиков.
     * дз1
     * @param boxList Список коробок для укладки.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(List<Box> boxList);
    /**
     * Укладывает коробки в грузовики с учетом ограничения по количеству.
     * @param truckList Список грузовиков, в которые будет производиться укладка.
     * @param maxTruckNumber Максимальное количество грузовиков для использования.
     * @param boxList Список коробок для укладки.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList);
    /**
     * Возвращает тип алгоритма укладки коробок.
     * @return Тип алгоритма, применяемого в стратегии.
     */
    TypeAlgorithm getTypeAlgorithm();
}



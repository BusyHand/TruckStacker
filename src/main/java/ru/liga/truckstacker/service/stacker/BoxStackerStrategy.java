
package ru.liga.truckstacker.service.stacker;

import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.util.TypeAlgorithm;

import java.util.List;

/**
 * Интерфейс для стратегий укладки коробок, определяющий методы для укладки.
 */
public interface BoxStackerStrategy {
    /**
     * Укладывает коробки без учета грузовиков.
     * дз1
     *
     * @param boxList Список коробок для укладки.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(List<Box> boxList);

    /**
     * Укладывает коробки в грузовики с учетом ограничения количества использования грузовиков.
     *
     * @param boxList        Список коробок для укладки.
     * @param maxTruckNumber Максимальное количество грузовиков для использования.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(List<Box> boxList, int maxTruckNumber);


    List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList);


    List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber);

    /**
     * Возвращает тип алгоритма укладки коробок.
     *
     * @return Тип алгоритма, применяемого в стратегии.
     */
    TypeAlgorithm getTypeAlgorithm();
}



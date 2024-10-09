
package ru.liga.truckstacker.service.stacker;

import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.util.Algorithm;

import java.util.List;

/**
 * Интерфейс для стратегий укладки коробок, определяющий методы для укладки.
 */
public interface BoxStackerStrategy {
    /**
     * Укладывает коробки без ограничения в количестве использования грузовиков.
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

    /**
     * Укладывает коробки в уже созданные грузовики, у которых уже проинициализирована высота и ширина.
     * Количество грузовиков ограничина размером передаваемого списка.
     *
     * @param trucks        Список коробок для укладки.
     * @param boxList        Список коробок для укладки.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList);

    /**
     * Укладывает коробки в ограниченное количество грузовиков,
     * которые будут создаваться по шаблону передаваемого на вход грузовика,
     *
     * @param truckExample          Грузовик, высота и ширина которого, будет шаблоном создания других грузовиков
     * @param boxList               Список коробок для укладки.
     * @param maxTruckNumber        Максимальное количество грузовиков для использования.
     * @return Список грузовиков с уложенными коробками.
     */
    List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber);

    /**
     * Возвращает тип алгоритма укладки коробок.
     *
     * @return Тип алгоритма, применяемого в стратегии.
     */
    Algorithm getAlgorithm();
}



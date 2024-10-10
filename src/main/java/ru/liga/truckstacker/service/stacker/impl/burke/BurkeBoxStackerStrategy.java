
package ru.liga.truckstacker.service.stacker.impl.burke;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.enums.Algorithm;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.service.stacker.impl.burke.logic.BurkeAlgorithm;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toCollection;
import static ru.liga.truckstacker.enums.Algorithm.BURKE;

/**
 * Стратегия укладки коробок с использованием алгоритма Бёрка.
 *
 * Основная идея алгоритма - это массив, по которому по мере заполнения полосы можно отслеживатьнаименее заполненные области и их ширину.
 * В начале он заполнен нулями.
 * Прямоугольники сортируются по не-возрастанию, внезапно, ширины.
 *
 * Затем, на каждом шаге алгоритма:
 * 1. Вычисляется позиция самой низкой области — индекс минимального значения массива;
 * 2. Выбирается наиболее подходящий прямоугольник — во-первых, помещающийся в эту область, во-вторых, максимально ее заполняющий по ширине;
 * 3. Если подходящий прямоугольник найден, он размещается в этой области одним из способов:
 *      3.1 По левому краю области;
 *      3.2 Ближе к более высокому соседу, если один из соседей — край полосы, то ближе к краю;
 *      3.3 Ближе к менее высокому соседу, если один из соседей — край полосы, то дальше от края. К значениям массива, соответствующим ширине прямоугольника, прибавляется его высота.
 * 4. Если подходящего прямоугольника нет, область «заполняется» путем выравнивания ее высоты до высоты ближайшего края.
 * Вам уже захотелось написать по этому алгоритму бота, играющего в Тетрис?
 *
 * Вот ссылка если прям сильно интересно https://habr.com/ru/articles/136225/
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BurkeBoxStackerStrategy implements BoxStackerStrategy {


    private final BurkeAlgorithm burkeAlgorithm = new BurkeAlgorithm();

    @Override
    public Algorithm getAlgorithm() {
        return BURKE;
    }


    @Override
    public List<Truck> stackBoxes(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        List<Truck> trucks = fillAllTrucksToLimitedList(truckExample, boxList, maxTruckNumber);
        return stackBoxes(trucks, boxList);
    }

    @Override
    public List<Truck> stackBoxes(List<Truck> trucks, List<Box> boxList) {
        return stackBoxesCommon(trucks, boxList, trucks.size());
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList) {
        log.info("Starting to stack boxes without truck limitation.");
        return stackBoxesCommon(boxList, Integer.MAX_VALUE);
    }

    @Override
    public List<Truck> stackBoxes(List<Box> boxList, int maxTruckNumber) {
        log.info("Starting to stack boxes onto trucks. Max truck number: {}", maxTruckNumber);
        return stackBoxesCommon(boxList, maxTruckNumber);
    }

    private List<Truck> stackBoxesCommon(List<Box> boxList, int maxTruckNumber) {
        return stackBoxesCommon(Collections.EMPTY_LIST, boxList, maxTruckNumber);
    }

    private List<Truck> stackBoxesCommon(List<Truck> trucks, List<Box> boxList, int maxTruckNumber) {
        log.debug("Input box list size: {}", boxList.size());

        List<HeightStatBox> heightStatBoxes = boxesToHeightStatBoxes(boxList);
        log.debug("Converted {} boxes to HeightStatBoxes.", heightStatBoxes.size());
        List<HeightStatTruck> heightStatTrucks = wrapHeightStatTrucks(trucks);

        List<HeightStatTruck> stackedTrucks;
        if (!heightStatTrucks.isEmpty()) {
            stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, heightStatBoxes);
        } else if (maxTruckNumber == Integer.MAX_VALUE) {
            stackedTrucks = burkeAlgorithm.stackBox(heightStatBoxes);
        } else {
            stackedTrucks = burkeAlgorithm.stackBoxInLimitedTrucks(heightStatTrucks, maxTruckNumber, heightStatBoxes);
        }

        log.info("Stacking completed. Number of trucks used: {}", stackedTrucks.size());
        return unwrapHeightStatTrucksConvertor(stackedTrucks);
    }

    private List<Truck> fillAllTrucksToLimitedList(Truck truckExample, List<Box> boxList, int maxTruckNumber) {
        int height = truckExample.getHeight();
        int width = truckExample.getWidth();
        maxTruckNumber = min(boxList.size(), maxTruckNumber);
        List<Truck> trucks = new ArrayList<>();

        for (int i = 0; i < maxTruckNumber; i++) {
            trucks.add(new Truck(height, width));
        }
        return trucks;
    }

    private List<HeightStatTruck> wrapHeightStatTrucks(List<Truck> trucks) {
        List<HeightStatTruck> heightStatTrucks;
        if (trucks.isEmpty()) {
            heightStatTrucks = new ArrayList<>();
        } else {
            heightStatTrucks = trucks.stream()
                    .map(HeightStatTruck::new)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return heightStatTrucks;
    }

    private List<HeightStatBox> boxesToHeightStatBoxes(List<Box> boxes) {
        return boxes.stream()
                .map(HeightStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(toCollection(ArrayList::new));
    }

    private List<Truck> unwrapHeightStatTrucksConvertor(List<HeightStatTruck> boxWrapperCollections) {
        return boxWrapperCollections.stream()
                .filter(HeightStatTruck::isNotEmpty)
                .map(HeightStatTruck::getTruck)
                .toList();
    }

}

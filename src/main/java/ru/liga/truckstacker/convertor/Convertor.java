package ru.liga.truckstacker.convertor;

import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatBox;
import ru.liga.truckstacker.service.stacker.impl.balanced.model.AreaStatTruck;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@UtilityClass
public class Convertor {

    public List<HeightStatTruck> wrapHeightStatTrucks(List<Truck> trucks) {
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

    public List<Truck> unwrapHeightStatTrucksConvertor(List<HeightStatTruck> boxWrapperCollections) {
        return boxWrapperCollections.stream()
                .filter(HeightStatTruck::isNotEmpty)
                .map(HeightStatTruck::getTruck)
                .toList();
    }

    public List<Truck> unwrapAreaStatTrucksConvertor(PriorityQueue<AreaStatTruck> truckPriorityQueue) {
        return truckPriorityQueue.stream()
                .filter(areaStatTruck -> !areaStatTruck.getTruck().isEmpty())
                .map(AreaStatTruck::getTruck)
                .toList();
    }

    public PriorityQueue<AreaStatTruck> trucksToPriorityQueueAreaStatTrucks(List<Truck> truckList) {
        return truckList.stream()
                .map(AreaStatTruck::new)
                .collect(toCollection(() -> new PriorityQueue<AreaStatTruck>(Comparator.reverseOrder())));
    }

    public Truck convertBoxToSingleTruck(Box box) {
        Truck truck = new Truck();
        if (!truck.canFit(0, 0, box)) {
            throw new IllegalArgumentException("Box cant stack");
        }
        truck.addBoxByCoordinates(0, 0, box);
        return truck;
    }

    public Deque<AreaStatBox> boxesToDequeAreaStatBoxes(List<Box> boxList) {
        return boxList.stream()
                .map(AreaStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    public List<Box> unpackTrucks(List<Truck> truckList) {
        return truckList.stream()
                .map(Convertor::unpackTrucks)
                .flatMap(List::stream)
                .collect(toCollection(ArrayList::new));
    }

    private List<Box> unpackTrucks(Truck truck) {
        return truck.getBoxesInTruck()
                .entrySet()
                .stream()
                .flatMap(entry -> Collections.nCopies(entry.getValue(), entry.getKey()).stream())
                .toList();
    }

    public List<HeightStatBox> boxesToHeightStatBoxes(List<Box> boxes) {
        return boxes.stream()
                .map(HeightStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(toCollection(ArrayList::new));
    }


}

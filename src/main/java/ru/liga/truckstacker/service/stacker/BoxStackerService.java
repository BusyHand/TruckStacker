
package ru.liga.truckstacker.service.stacker;

import ru.liga.truckstacker.config.TypeAlgorithm;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.List;


public interface BoxStackerService {

    List<Truck> stackBoxes(List<Box> boxList);

    List<Truck> stackBoxes(List<Truck> truckList, int maxTruckNumber, List<Box> boxList);

    TypeAlgorithm getTypeAlgorithm();
}



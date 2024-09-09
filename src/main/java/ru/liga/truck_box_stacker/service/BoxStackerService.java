
package ru.liga.truck_box_stacker.service;

import java.util.List;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.TruckList;
/**
 * Interface representing a service for stacking boxes into trucks.
 * This service defines a contract for implementations to provide
 * specific algorithms for stacking boxes based on certain criteria.
 */
public interface BoxStackerService {
    /**
     * Stacks a list of boxes into a TruckList.
     *
     * This method accepts a list of Box objects and organizes them
     * into a TruckList according to the specific stacking logic
     * implemented by the concrete class.
     *
     * @param boxList a list of Box objects to be stacked. It cannot be null
     *                and should contain at least one box for processing.
     * @return a TruckList containing the stacked boxes arranged based
     *         on the implementation's logic. If the input list is empty,
     *         an empty TruckList may be returned.
     * @throws IllegalArgumentException if boxList is null or contains
     *                                  invalid Box objects.
     */
    TruckList stackBoxes(List<Box> boxList);
}



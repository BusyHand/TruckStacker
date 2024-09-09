package ru.liga.truck_box_stacker.service.impl.dummy;

import org.springframework.stereotype.Component;
import ru.liga.truck_box_stacker.config.GlobalSettings;
import ru.liga.truck_box_stacker.config.bpp.annotation.UseStackerWhen;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.Truck;
import ru.liga.truck_box_stacker.model.TruckList;
import ru.liga.truck_box_stacker.service.BoxStackerService;

import java.util.List;
import java.util.Objects;

import static ru.liga.truck_box_stacker.model.TypeAlgorithm.A_LOT_OF_DATA;

/**
 * A dummy implementation of the BoxStackerService that stacks boxes
 * into trucks based on their dimensions.
 *
 * This service uses a very simple stacking algorithm: it checks if
 * each box can fit into a truck based on global height and width
 * settings. If the box fits, it populates the truck's area with the
 * box pattern; otherwise, it discards the box.
 *
 * The algorithm has a time complexity of O(n), where n is the number
 * of boxes to stack. Each box is evaluated individually, leading to
 * linear complexity.
 */
@Component
@UseStackerWhen(A_LOT_OF_DATA)
public class DummyBoxStackerServiceImpl implements BoxStackerService {

    /**
     * Stacks boxes into trucks and returns a TruckList containing
     * the stacked trucks.
     *
     * This method processes the provided list of boxes, calling the
     * internal method to generate a TruckList. Each box is evaluated
     * to determine if it can fit into a truck based on specific
     * global settings for width and height.
     *
     * @param boxList a List of Box objects to be stacked
     *                into trucks. The list should not be null
     *                and can contain any number of Box objects.
     * @return a TruckList containing the trucks that were able
     *         to stack the provided boxes. If no boxes fit,
     *         an empty TruckList is returned.
     */
    @Override
    public TruckList stackBoxes(List<Box> boxList) {
        return new TruckList(getTruckList(boxList));
    }

    /**
     * Creates a list of trucks based on the provided list of boxes.
     *
     * This private method iterates through each Box in the input list,
     * checking dimensions against predefined global settings. If a
     * box can fit, it populates a new Truck's area with the box's pattern;
     * otherwise, it ignores the box.
     *
     * @param boxList a List of Box objects to evaluate and stack.
     * @return a List of Truck objects, each representing a
     *         successfully stacked box. Any boxes that do not fit
     *         are excluded from the result.
     */
    private List<Truck> getTruckList(List<Box> boxList) {
        return boxList.stream()
                .map(box -> {
                    Truck truck = new Truck();
                    int[][] truckArea = truck.getTruckArea();
                    int[][] boxPattern = box.getBoxPattern();

                    // Check if the box fits within specified truck dimensions
                    if (box.getWidth() <= GlobalSettings.WIDTH_TRUCK
                            && box.getHeight() <= GlobalSettings.HEIGHT_TRUCK) {
                        for (int i = 0; i < box.getHeight(); i++) {
                            for (int j = 0; j < box.getWidth(); j++) {
                                truckArea[i][j] = boxPattern[i][j];
                            }
                        }
                    } else {
                        return null;  // Box does not fit, return null
                    }
                    return truck;  // Box fits, return the populated truck
                })
                .filter(Objects::nonNull) // Filter out null results (boxes that didn't fit)
                .toList();  // Collect the valid trucks into a list
    }
}


package ru.liga.truck_box_stacker.convertor;

import lombok.extern.slf4j.Slf4j;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.BoxType;

import java.util.Arrays;
import java.util.Optional;
/**
 * Utility class for converting a string representation of a box type
 * to a corresponding Box object. This class utilizes the BoxType enum
 * to perform the conversion and includes fallback handling for
 * unrecognized string inputs.
 */
@Slf4j
public class ConvertStringToBox {
    /**
     * Converts a string representation of a box type to a Box object.
     *
     * This method searches through the BoxType enum values to find an
     * associated box type that matches the provided string. If no match
     * is found, it defaults to BoxType.ONE and logs a warning.
     *
     * @param strBox a string representation of the box type to be converted.
     * @return a Box object corresponding to the provided string representation.
     */
    public static Box convert(String strBox) {
        return Arrays.stream(BoxType.values())
                .filter(boxType -> boxType.getBox().getStringInterpretation().equals(strBox))
                .findFirst()
                .or(() -> {
                    log.warn("Not Converted box changed to box type ONE");
                    return Optional.of(BoxType.ONE);
                })
                .get()
                .getBox();
    }
}

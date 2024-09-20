
package ru.liga.truckstacker.convertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.liga.truckstacker.model.Box;

/**
 * Utility class for converting a string representation of a box type
 * to a corresponding Box object. This class utilizes the BoxType enum
 * to perform the conversion and includes fallback handling for
 * unrecognized string inputs.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringToBoxConvertor {

    public static Box convert(String strBox) {
        for (BoxType boxType : BoxType.values()) {
            if (boxType.getBox().representation().equals(strBox)) {
                return boxType.getBox();
            }
        }

        log.warn("Not Converted box; changed to box type ONE");
        return BoxType.ONE.getBox();
    }
}

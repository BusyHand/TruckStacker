
package ru.liga.truckstacker.convertor.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.model.BoxType;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;

@Slf4j
@Component
public class StringBoxConvertor implements Convertor<String, Box> {

    @Override
    public Box convert(String strBox) {
        for (BoxType boxType : BoxType.values()) {
            if (boxType.getBox().representation().equals(strBox)) {
                return boxType.getBox();
            }
        }
        log.warn("Not Converted box; changed to box type ONE");
        return BoxType.ONE.getBox();
    }
}

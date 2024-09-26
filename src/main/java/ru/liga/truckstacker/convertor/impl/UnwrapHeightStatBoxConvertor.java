package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatBox;

import java.util.List;


@Component
public class UnwrapHeightStatBoxConvertor implements Convertor<List<HeightStatBox>, List<Box>> {

    @Override
    public List<Box> convert(List<HeightStatBox> boxWrapperCollections) {
        return boxWrapperCollections.stream()
                .map(HeightStatBox::getBox)
                .toList();
    }
}

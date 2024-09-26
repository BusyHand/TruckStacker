package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.stacker.strategy.burke.model.HeightStatBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

@Component
public class BoxesHeightStatBoxConvertor implements Convertor<List<Box>, List<HeightStatBox>> {
    @Override
    public List<HeightStatBox> convert(List<Box> boxes) {
        return boxes.stream()
                .map(HeightStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(toCollection(ArrayList::new));
    }
}

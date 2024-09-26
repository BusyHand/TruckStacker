package ru.liga.truckstacker.convertor.impl;

import org.springframework.stereotype.Component;
import ru.liga.truckstacker.convertor.Convertor;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.stacker.strategy.balanced.model.AreaStatBox;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoxesToAreaStatBoxes implements Convertor<List<Box>, Deque<AreaStatBox>> {
    @Override
    public Deque<AreaStatBox> convert(List<Box> boxList) {
        return boxList.stream()
                .map(AreaStatBox::new)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(ArrayDeque::new));
    }
}

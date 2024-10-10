package ru.liga.truckstacker.service.boxgetter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.mapper.BoxMapper;
import ru.liga.truckstacker.parser.BoxFileParser;
import ru.liga.truckstacker.service.boxgetter.StackingRequestBoxesGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация {@link StackingRequestBoxesGetter},
 * которая получает коробки из файла пользователся с коробками в json формате.
 */
@Component
@RequiredArgsConstructor
public class StackingRequestBoxesGetterFromFile implements StackingRequestBoxesGetter {

    private final BoxFileParser boxFileParser;
    private final BoxMapper boxMapper;

    @Override
    public List<Box> getBoxes(StackingRequest stackingRequest) {
        return boxFileParser.parseBoxFile(stackingRequest.getBoxesFileName())
                .stream()
                .map(boxMapper::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

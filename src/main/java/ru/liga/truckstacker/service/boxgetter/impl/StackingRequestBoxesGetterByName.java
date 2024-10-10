package ru.liga.truckstacker.service.boxgetter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.parser.StackingRequestParser;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.mapper.BoxMapper;
import ru.liga.truckstacker.service.BoxService;
import ru.liga.truckstacker.service.boxgetter.StackingRequestBoxesGetter;

import java.util.List;
/**
 * Реализация {@link StackingRequestBoxesGetter},
 * которая получает коробки по их именам из запроса укладки.
 */
@Component
@RequiredArgsConstructor
public class StackingRequestBoxesGetterByName implements StackingRequestBoxesGetter {

    private final BoxService boxService;

    private final BoxMapper boxMapper;

    private final StackingRequestParser stackingRequestParser;


    @Override
    public List<Box> getBoxes(StackingRequest stackingRequest) {
        List<String> boxNames = stackingRequestParser.getBoxNamesList(stackingRequest);
        return boxNames.stream()
                .map(boxService::getBoxByName)
                .map(boxMapper::toEntity)
                .toList();
    }
}

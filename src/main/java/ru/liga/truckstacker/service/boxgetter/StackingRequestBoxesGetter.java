package ru.liga.truckstacker.service.boxgetter;

import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Box;

import java.util.List;

public interface StackingRequestBoxesGetter {

    List<Box> getBoxes(StackingRequest stackingRequest);

}

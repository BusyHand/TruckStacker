package ru.liga.truckstacker.service.stacker.request;

import lombok.Builder;
import lombok.Getter;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

@Builder
@Getter
public class BoxStackingRequest {

    private final List<Truck> trucks;
    private final List<Box> boxes;
    private final int maxSizeNumberOfTrucks;
    private final String typeAlgorithmName;
}

package ru.liga.truckstacker.controller;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StackingRequest {

    private final String trucksFileName;
    private final String boxesFileName;
    private final String typeAlgorithmName;
    private final int maxSizeNumberOfTrucks;
}

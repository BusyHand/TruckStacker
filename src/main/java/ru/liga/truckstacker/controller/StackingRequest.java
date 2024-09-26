package ru.liga.truckstacker.controller;

import lombok.Builder;
import lombok.Getter;
/**
 * Класс, представляющий запрос от пользователя на укладку коробок в грузовики.
 */
@Builder
@Getter
public class StackingRequest {

    private final String trucksFileName;
    private final String boxesFileName;
    private final String typeAlgorithmName;
    private final int maxSizeNumberOfTrucks;
}

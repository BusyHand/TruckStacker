package ru.liga.truckstacker.controller;

import lombok.Builder;
import lombok.Getter;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.List;
/**
 * Запрос на укладку коробок в грузовики.
 * Этот класс представляет собой запрос, который содержит информацию о
 * грузовиках, коробках, максимальном количестве грузовиков и типе алгоритма,
 * используемом для укладки коробок.
 */
@Builder
@Getter
public class BoxStackingRequest {

    private final List<Truck> trucks;
    private final List<Box> boxes;
    private final int maxSizeNumberOfTrucks;
    private final String typeAlgorithmName;
}

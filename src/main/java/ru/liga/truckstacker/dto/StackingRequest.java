package ru.liga.truckstacker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс, представляющий запрос от пользователя на укладку коробок в грузовики.
 */
@Builder
@Getter
@Setter
@ToString
public class StackingRequest {

    private String alreadyFilledTrucksFileName;
    private String boxesFileName;
    private String typeAlgorithmName;
    private int limitNumberOfTrucks;
    private String truckSize;
    private String boxNames;
}

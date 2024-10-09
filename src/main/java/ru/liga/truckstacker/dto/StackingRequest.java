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
@ToString
public class StackingRequest {

    @Builder.Default
    private final String trucksFileName = "";

    @Builder.Default
    private final String boxesFileName = "";

    @Builder.Default
    private final String typeAlgorithmName = "burke";

    @Builder.Default
    private final int limitNumberOfTrucks = Integer.MAX_VALUE;

    @Builder.Default
    private final String truckSize = "";

    @Builder.Default
    private final String boxNames = "";
}

package ru.liga.truckstacker.config.bpp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@AllArgsConstructor
public enum TypeAlgorithm {

    BURKE(5000),

    BALANCED(10_000),

    DUMMY(Integer.MAX_VALUE);


    @Getter
    private final int maxSizeOfDataToUseAlgorithm;

    public static TypeAlgorithm getTypeAlgorithmByAlgorithmName(String algorithmName) {
        return Arrays.stream(TypeAlgorithm.values())
                .filter(type -> type.name().equalsIgnoreCase(algorithmName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No match with algorithm name:" + algorithmName));
    }

}

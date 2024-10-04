package ru.liga.truckstacker.util;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * Перечисление типов алгоритмов для укладки коробок.
 */
public enum TypeAlgorithm {

    BURKE,

    BALANCED,

    DUMMY;

    /**
     * Получает тип алгоритма по его имени.
     * @param algorithmName Имя алгоритма.
     * @return Соответствующий тип алгоритма.
     * @throws IllegalArgumentException Если алгоритм не найден.
     */
    public static TypeAlgorithm getTypeAlgorithmByAlgorithmName(String algorithmName) {
        return Arrays.stream(TypeAlgorithm.values())
                .filter(type -> type.name().equalsIgnoreCase(algorithmName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No match with algorithm name:" + algorithmName));
    }

}

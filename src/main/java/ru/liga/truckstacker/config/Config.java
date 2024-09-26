package ru.liga.truckstacker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class Config {

    /**
     * Создает отображение типов алгоритмов и соответствующих стратегий укладки коробок.
     * @param boxStackerStrategies Список стратегий укладки коробок.
     * @return Мапа, в которой ключи — это типы алгоритмов, а значения — соответствующие стратегии.
     */
    @Bean
    public Map<TypeAlgorithm, BoxStackerStrategy> getTypeAlgorithmToBoxStackerService(List<BoxStackerStrategy> boxStackerStrategies) {
        return boxStackerStrategies
                .stream()
                .collect(Collectors.toMap(BoxStackerStrategy::getTypeAlgorithm, Function.identity()));
    }

}

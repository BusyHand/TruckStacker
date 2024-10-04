package ru.liga.truckstacker.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.liga.truckstacker.service.BoxService;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.util.StartDataInDb;
import ru.liga.truckstacker.util.TypeAlgorithm;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class AppConfig {

    /**
     * Создает отображение типов алгоритмов и соответствующих стратегий укладки коробок.
     *
     * @param boxStackerStrategies Список стратегий укладки коробок.
     * @return Мапа, в которой ключи — это типы алгоритмов, а значения — соответствующие стратегии.
     */
    @Bean
    public Map<TypeAlgorithm, BoxStackerStrategy> getTypeAlgorithmToBoxStackerStrategyFactory(List<BoxStackerStrategy> boxStackerStrategies) {
        return boxStackerStrategies
                .stream()
                .collect(Collectors.toMap(BoxStackerStrategy::getTypeAlgorithm, Function.identity()));
    }


}

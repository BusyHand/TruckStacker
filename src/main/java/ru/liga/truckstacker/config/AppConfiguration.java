package ru.liga.truckstacker.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.liga.truckstacker.service.stacker.BoxStackerStrategy;
import ru.liga.truckstacker.enums.Algorithm;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
public class AppConfiguration {

    /**
     * Создает мапу где ключ это алгоритм, а значение это соответствующая стратегия укладки коробок.
     *
     * @param boxStackerStrategies Список стратегий укладки коробок.
     * @return Мапа, в которой ключи — это типы алгоритмов, а значения — соответствующие стратегии.
     */
    @Bean
    public Map<Algorithm, BoxStackerStrategy> getAlgorithmToBoxStackerStrategy(List<BoxStackerStrategy> boxStackerStrategies) {
        return boxStackerStrategies
                .stream()
                .collect(Collectors.toMap(BoxStackerStrategy::getAlgorithm, Function.identity()));
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


}

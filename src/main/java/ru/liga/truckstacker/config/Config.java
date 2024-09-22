package ru.liga.truckstacker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.liga.truckstacker.service.stacker.BoxStackerService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class Config {


    @Bean
    public Map<TypeAlgorithm, BoxStackerService> getTypeAlgorithmToBoxStackerService(List<BoxStackerService> boxStackerServices) {
        return boxStackerServices.stream()
                .collect(Collectors.toMap(BoxStackerService::getTypeAlgorithm, Function.identity()));
    }

}

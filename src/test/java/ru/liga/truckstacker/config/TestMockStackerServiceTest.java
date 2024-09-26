package ru.liga.truckstacker.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.liga.truckstacker.service.stacker.strategy.burke.BurkeBoxStackerStrategy;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@TestConfiguration
public class TestMockStackerServiceTest {

    @Bean
    public BurkeBoxStackerStrategy burkeBoxStackerStrategy() {
        BurkeBoxStackerStrategy mockService = Mockito.mock(BurkeBoxStackerStrategy.class);
        // Define the behavior of your mocked service
        when(mockService.getTypeAlgorithm()).thenReturn(TypeAlgorithm.BURKE);

        // You can also add other method stubbing here
        // For example:
        when(mockService.stackBoxes(anyList(), anyInt(), anyList())).thenReturn(Collections.emptyList());

        return mockService;
    }
}

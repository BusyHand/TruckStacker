package ru.liga.truckstacker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.liga.truckstacker.runner.MainCommandLineRunner;

@TestConfiguration
public class TestMockBeanConfig {

    @MockBean
    public MainCommandLineRunner mainCommandLineRunner;
}
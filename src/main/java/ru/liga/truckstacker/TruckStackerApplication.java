package ru.liga.truckstacker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.liga.truckstacker.config.AppConfig;
import ru.liga.truckstacker.service.BoxService;
import ru.liga.truckstacker.util.StartDataInDb;

import java.io.File;

@SpringBootApplication
public class TruckStackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruckStackerApplication.class, args);
    }
}
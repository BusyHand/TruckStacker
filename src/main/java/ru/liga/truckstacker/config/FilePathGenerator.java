package ru.liga.truckstacker.config;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FilePathGenerator {

    private static final String FILENAME = "trucks";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DIRECTORY = "db";
    private static final String SUBDIRECTORY = "trucks";
    private static int count = 1;

    public Path generateTruckFilePath() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        return Paths.get(DIRECTORY, SUBDIRECTORY, FILENAME + "-" + date + "-" + (count++) + ".json");
    }
}

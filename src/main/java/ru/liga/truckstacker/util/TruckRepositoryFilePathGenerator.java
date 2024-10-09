package ru.liga.truckstacker.util;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Генератор путей для файлов с грузовиками.
 */
@UtilityClass
public class TruckRepositoryFilePathGenerator {

    private static final String FILENAME = "trucks";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DIRECTORY = "db";
    private static final String SUBDIRECTORY = "trucks";
    private static final String SEPARATOR = "-";
    private static final String JSON_FILE_TYPE = ".json";
    private static final AtomicInteger count = new AtomicInteger(1);

    /**
     * Генерирует путь для файла с грузовиками.
     * Путь формируется с использованием текущей даты и счетчика для уникальности имен файлов.
     * @return Путь к файлу с грузовиками.
     */
    public Path generateTruckFilePath() {
        final String DATE = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        final int currentCount = count.getAndIncrement();
        final String FULL_FILENAME = FILENAME + SEPARATOR + DATE + SEPARATOR + currentCount + JSON_FILE_TYPE;
        return Paths.get(DIRECTORY, SUBDIRECTORY, FULL_FILENAME);
    }
}

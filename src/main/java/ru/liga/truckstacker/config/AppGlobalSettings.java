package ru.liga.truckstacker.config;

import lombok.experimental.UtilityClass;

import java.nio.file.Path;


@UtilityClass
public class AppGlobalSettings {
    public static final int DEFAULT_HEIGHT_TRUCK = 6;
    public static final int DEFAULT_WIDTH_TRUCK = 6;
    public static final char EMPTY_CELL = ' ';

    public static final Path DB_FILES_DIRECTORIES = Path.of("db", "files");

}

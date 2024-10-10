package ru.liga.truckstacker.reader;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.liga.truckstacker.config.AppGlobalSettings.DB_FILES_DIRECTORIES;

/**
 * Класс для того чтобы выше не писать try catch
 */
@Component
public class FileReader {

    /**
     * Читает все строки из указанного файла и возвращает их в виде строки.
     *
     * @param filename Имя файла для чтения.
     * @return Содержимое файла в виде строки.
     * @throws RuntimeException Если произошла ошибка при чтении или конвертации файла.
     */
    public String readAllLines(String filename) {
        Path trucksFilesPath = DB_FILES_DIRECTORIES.resolve(filename);
        try {
            return Files.readString(trucksFilesPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or converting file", e);//todo
        }
    }
}

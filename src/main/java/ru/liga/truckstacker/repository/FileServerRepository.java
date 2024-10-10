package ru.liga.truckstacker.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static ru.liga.truckstacker.config.AppGlobalSettings.DB_FILES_DIRECTORIES;

/**
 * Класс который сохраняет файл на сервере в папке db/files
 * да-да той самой которую нужно в resources ;D
 *
 * Сохраняю не в базе потому-что увидел комментарий что профитов от бд не особо много
 */
@Repository
@RequiredArgsConstructor
public class FileServerRepository {

    /**
     * Сохраняет файл на сервере в папке db/files
     *
     * @param file     массив байтов, представляющий содержимое файла.
     * @param fileName имя файла, под которым будет сохранено содержимое.
     * @throws RuntimeException если произошла ошибка при сохранении файла.
     */
    public void saveFile(byte[] file, String fileName) {
        if (file != null) {
            try {
                Files.createDirectories(DB_FILES_DIRECTORIES);
                Path filePath = DB_FILES_DIRECTORIES.resolve(fileName);
                Files.write(filePath, file, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + fileName, e);
            }
        }
    }
}

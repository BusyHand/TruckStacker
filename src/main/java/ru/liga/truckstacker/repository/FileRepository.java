package ru.liga.truckstacker.repository;


import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.liga.truckstacker.util.GlobalSettings.DB_FILES_DIRECTORIES;

@Repository
public class FileRepository {

    private final List<String> dbTruck = new ArrayList<>();
    private final List<String> dbBox = new ArrayList<>();

    public List<String> getAllTrucksFiles() {
        return Collections.unmodifiableList(dbTruck);
    }

    public List<String> getAllBoxesFiles() {
        return Collections.unmodifiableList(dbBox);
    }

    public void saveTruckFile(byte[] file, String fileName) {
        saveFile(file, fileName);
        dbTruck.add(fileName);
    }

    public void saveBoxFile(byte[] file, String fileName) {
        saveFile(file, fileName);
        dbBox.add(fileName);
    }

    private void saveFile(byte[] file, String fileName) {
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

package ru.liga.truckstacker.service.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.truckstacker.entity.FileEntity;
import ru.liga.truckstacker.enums.FileType;
import ru.liga.truckstacker.repository.FileEntityRepository;
import ru.liga.truckstacker.repository.FileServerRepository;

import java.util.List;

/**
 * Класс который управляет файлами, загруженными пользователем из телеграмма
 */
@Service
@RequiredArgsConstructor
public class FileService {
    private final TelegramFileDownloader telegramFileDownloader;

    private final FileEntityRepository fileEntityRepository;

    private final FileServerRepository fileServerRepository;

    /**
     * Возвращает все названия файлов (чтобы пользователь мог выбрать, какой именно он будет использовать для упаковки),
     * загруженные пользователем, которые содержат грузовики в формате JSON.
     *
     * @return List<String> Список названий файлов, содержащих грузовики.
     */
    public List<String> getAllTrucksFiles() {
        List<FileEntity> fileEntities = fileEntityRepository.findFileEntitiesByFileType(FileType.TRUCK_FILE);
        return fileEntities.stream()
                .map(FileEntity::getFileName)
                .toList();
    }

    /**
     * Возвращает все названия файлов (чтобы пользователь мог выбрать, какой именно он будет использовать для упаковки),
     * загруженные пользователем, которые содержат коробки в формате JSON.
     *
     * @return List<String> Список названий файлов, содержащих коробки.
     */
    public List<String> getAllBoxesFiles() {
        List<FileEntity> fileEntities = fileEntityRepository.findFileEntitiesByFileType(FileType.BOX_FILE);
        return fileEntities.stream()
                .map(FileEntity::getFileName)
                .toList();
    }


    /**
     * Скачивает файл с сервера телеграмма, сохраняет файл на сервера в папке db/files (тот самый которыйы вы говорите спрятать в resources)
     * создает FileEntity и затем сохраняет его в базе чтобы не потеярять информацию о его существование
     */
    @Transactional
    public void processDownloadAndSaveOnServerFile(TelegramBot telegramBot, Message message) {
        Document document = message.document();
        if (document == null) {
            return;
        }
        String caption = message.caption();
        if (caption == null) {
            return;
        }

        String fileName = document.fileName();
        FileType fileType = switch (caption.trim().toLowerCase()) {
            case "/save-boxes-file" -> FileType.BOX_FILE;
            case "/save-truck-file" -> FileType.TRUCK_FILE;
            default -> throw new IllegalArgumentException("Command not correct");
        };
        byte[] file = telegramFileDownloader.downloadFileFromTelegram(telegramBot, document);
        fileServerRepository.saveFile(file, fileName);
        FileEntity fileEntity = new FileEntity(fileName, fileType);
        fileEntityRepository.save(fileEntity);
    }
}

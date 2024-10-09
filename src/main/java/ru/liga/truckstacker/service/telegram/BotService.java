package ru.liga.truckstacker.service.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.repository.FileRepository;
import ru.liga.truckstacker.service.telegram.TelegramFileDownloader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotService {
    private final TelegramFileDownloader telegramFileDownloader;
    private final FileRepository fileRepository;

    //TODO transactional
    public void processDownloadAndSaveFile(TelegramBot telegramBot, Message message) {
        Document document = message.document();
        if (document != null) {
            String fileName = document.fileName();
            String caption = message.caption();
            if (caption != null) {
                byte[] file = telegramFileDownloader.downloadFileFromTelegram(telegramBot, document);
                switch (caption.toLowerCase()) {
                    case "/save-boxes-file" -> fileRepository.saveBoxFile(file, fileName);
                    case "/save-truck-file" -> fileRepository.saveTruckFile(file, fileName);
                    default -> throw new IllegalArgumentException("Command not correct");
                }
            }
        }
    }

    public List<String> getAllTrucksFiles() {
        return fileRepository.getAllTrucksFiles();
    }

    public List<String> getBoxesFiles() {
        return fileRepository.getAllBoxesFiles();
    }
}
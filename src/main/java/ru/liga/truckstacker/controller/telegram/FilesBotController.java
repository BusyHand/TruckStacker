package ru.liga.truckstacker.controller.telegram;

import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import ru.liga.truckstacker.config.telegram.TelegramMvcControllerProcessor;
import ru.liga.truckstacker.service.telegram.FileService;

import java.util.List;

@BotController
@RequiredArgsConstructor
public class FilesBotController extends TelegramMvcControllerProcessor {

    private final FileService fileService;

    @MessageRequest
    public void saveFile(TelegramBot telegramBot, Message message) {
        fileService.processDownloadAndSaveOnServerFile(telegramBot, message);
    }

    @MessageRequest("/get-all-truck-files")
    public List<String> getAllTruckFiles() {
        return fileService.getAllTrucksFiles();
    }

    @MessageRequest("/get-all-box-files")
    public List<String> getAllBoxFiles() {
        return fileService.getAllBoxesFiles();
    }
}

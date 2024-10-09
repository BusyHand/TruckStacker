package ru.liga.truckstacker.service.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramFileDownloader {
    private final RestTemplate restTemplate;

    public byte[] downloadFileFromTelegram(TelegramBot telegramBot, Document document) {
        String fileId = document.fileId();
        GetFileResponse getFileResponse = telegramBot.execute(new GetFile(fileId));
        String filePath = getFileResponse.file().filePath();
        String fileUrl = "https://api.telegram.org/file/bot" + telegramBot.getToken() + "/" + filePath;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(fileUrl, HttpMethod.GET, null, byte[].class, headers);
        return responseEntity.getBody();
    }
}
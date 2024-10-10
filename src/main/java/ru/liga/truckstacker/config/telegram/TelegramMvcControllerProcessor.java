package ru.liga.truckstacker.config.telegram;

import com.github.kshashov.telegram.api.TelegramMvcController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Всем бот контроллерам нужно наследоваться от этого класса, иначе не будут работать
 */
@RequiredArgsConstructor
public abstract class TelegramMvcControllerProcessor implements TelegramMvcController {

    @Value("${telegram.bot.key}")
    private String token;

    @Override
    public String getToken() {
        return token;
    }
}
package ru.liga.truckstacker.config.telegram;

import com.github.kshashov.telegram.config.TelegramBotGlobalProperties;
import com.github.kshashov.telegram.config.TelegramBotGlobalPropertiesConfiguration;
import com.pengrad.telegrambot.request.SetWebhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secrets.properties")
public class BotConfiguration implements TelegramBotGlobalPropertiesConfiguration {

    @Value("${telegram.bot.key}")
    private String botToken;

    @Value("${telegram.webhook-url}")
    private String webhookUrl;

    @Override
    public void configure(TelegramBotGlobalProperties.Builder builder) {
        builder.configureBot(botToken, botBuilder -> {
            botBuilder.useWebhook(new SetWebhook().url(webhookUrl));
        });
    }
}

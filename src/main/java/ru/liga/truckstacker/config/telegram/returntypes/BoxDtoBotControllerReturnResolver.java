package ru.liga.truckstacker.config.telegram.returntypes;

import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.handler.processor.response.BotHandlerMethodReturnValueHandler;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.BoxDto;

import javax.validation.constraints.NotNull;

@Component
public class BoxDtoBotControllerReturnResolver implements BotHandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(@NotNull MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        return BoxDto.class.isAssignableFrom(paramType);
    }

    @Nullable
    @Override
    public BaseRequest handleReturnValue(@Nullable Object returnValue, @NotNull MethodParameter returnType, @NotNull TelegramRequest telegramRequest) {
        BoxDto box = (BoxDto) returnValue;
        String result = "```java\n" +
                box.toString() +
                "\n```";
        return !result.isBlank() && telegramRequest.getChat() != null
                ? new SendMessage(telegramRequest.getChat().id(), result).parseMode(ParseMode.MarkdownV2)
                : null;
    }
}

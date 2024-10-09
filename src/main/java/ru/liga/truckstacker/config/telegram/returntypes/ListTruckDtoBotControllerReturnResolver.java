package ru.liga.truckstacker.config.telegram.returntypes;

import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.handler.processor.response.BotHandlerMethodReturnValueHandler;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.dto.TruckDto;

import javax.validation.constraints.NotNull;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Component
public class ListTruckDtoBotControllerReturnResolver implements BotHandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(@NotNull MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        if (List.class.isAssignableFrom(paramType)) {
            Type genericReturnType = returnType.getGenericParameterType();
            if (genericReturnType instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class<?> actualType) {
                    return TruckDto.class.isAssignableFrom(actualType);
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public BaseRequest handleReturnValue(@Nullable Object returnValue, @NotNull MethodParameter returnType, @NotNull TelegramRequest telegramRequest) {
        List<TruckDto> truckDtos = (List<TruckDto>) returnValue;
        StringBuilder outputValue = new StringBuilder();
        outputValue.append("```java\n");
        for (TruckDto truckDto : truckDtos) {
            outputValue.append(truckDto.toString()).append("\n");
        }
        outputValue.append("\n```");
        String result = outputValue.toString();
        return !result.isBlank() && telegramRequest.getChat() != null
                ? new SendMessage(telegramRequest.getChat().id(), result).parseMode(ParseMode.MarkdownV2)
                : null;
    }
}

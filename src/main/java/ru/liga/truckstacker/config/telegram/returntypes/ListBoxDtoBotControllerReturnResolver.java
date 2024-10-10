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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
/**
 * Это конвертор возвращемого значения из бот контроллеров, в данном случае
 * List<BoxDto> конвертируется в telegram code block message
 */
@Component
public class ListBoxDtoBotControllerReturnResolver implements BotHandlerMethodReturnValueHandler {

    /**
     * Проверка что возвращаемое значение List<BoxDto>
     */
    @Override
    public boolean supportsReturnType(@NotNull MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        if (List.class.isAssignableFrom(paramType)) {
            Type genericReturnType = returnType.getGenericParameterType();
            if (genericReturnType instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class<?> actualType) {
                    return BoxDto.class.isAssignableFrom(actualType);
                }
            }
        }
        return false;
    }

    /**
     * Если метод выше (supportsReturnType) возвратил true, то идет его конвертация в BaseRequest
     */
    @Nullable
    @Override
    public BaseRequest handleReturnValue(@Nullable Object returnValue, @NotNull MethodParameter returnType, @NotNull TelegramRequest telegramRequest) {
        List<BoxDto> boxes = (List<BoxDto>) returnValue;
        StringBuilder outputValue = new StringBuilder();
        outputValue.append("```java\n");
        for (BoxDto box : boxes) {
            outputValue.append(box.toString()).append("\n");
        }
        outputValue.append("\n```");
        String result = outputValue.toString();
        return !result.isBlank() && telegramRequest.getChat() != null
                ? new SendMessage(telegramRequest.getChat().id(), result).parseMode(ParseMode.MarkdownV2)
                : null;
    }
}

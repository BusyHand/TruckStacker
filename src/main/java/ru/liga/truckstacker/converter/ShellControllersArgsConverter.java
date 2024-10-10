package ru.liga.truckstacker.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Конвертер для преобразования аргументов контроллеров из строкового представления.
 * Специально для shell контроллера так как он не может распознать "\n".
 */
@Component
public class ShellControllersArgsConverter implements Converter<String, String> {

    /**
     * Конвертирует строку, заменяя последовательности "\\n" на фактические символы новой строки.
     *
     * @param source Исходная строка, которую необходимо конвертировать.
     * @return Конвертированная строка с замененными символами новой строки.
     */
    @Override
    public String convert(String source) {
        return source.replace("\\n", "\n");
    }
}

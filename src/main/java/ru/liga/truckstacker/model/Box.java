package ru.liga.truckstacker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Arrays;

import static java.lang.Math.max;

/**
 * Объект, представляющий коробку, имеющую заданные высоту, ширину, отображение и шаблон.
 */
@Getter
@EqualsAndHashCode
public class Box {
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @NotBlank
    private final String form;
    @NotBlank
    private final char symbol;
    @JsonIgnore
    private final int height;
    @JsonIgnore
    private final int width;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private final char[][] boxPattern;

    @JsonCreator
    public Box(@JsonProperty("name") String name,
               @JsonProperty("form") String form,
               @JsonProperty("symbol") char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.form = form.trim().replaceAll("[^~\\n\\s]", symbol + "");
        this.boxPattern = calculateBoxPattern();
        this.height = boxPattern.length;
        this.width = boxPattern[0].length;
    }

    private char[][] calculateBoxPattern() {
        String[] rows = form.split("\n");
        int maxLength = Integer.MIN_VALUE;
        for (String row : rows) {
            maxLength = max(maxLength, row.length());
        }
        char[][] boxPattern = new char[rows.length][maxLength];
        Arrays.stream(boxPattern)
                .forEach(t -> Arrays.fill(t, ' '));
        for (int i = rows.length - 1, k = 0; i >= 0; i--, k++) {
            char[] chars = rows[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                boxPattern[k][j] = chars[j];
            }
        }
        return boxPattern;
    }

    @Override
    public String toString() {
        return "\nBox{" +
                "name='" + name + '\'' +
                ", symbol=" + symbol +
                ", form=\'\n" + form + "\'}\n";
    }

}




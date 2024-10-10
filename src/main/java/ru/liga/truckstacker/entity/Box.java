package ru.liga.truckstacker.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

import static java.lang.Math.max;

/**
 * Объект, представляющий коробку, имеющую заданные высоту, ширину, отображение и шаблон.
 */
@Entity
@Table(name = "boxes")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Box {
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "form")
    private String form;
    @Column(name = "symbol")
    private char symbol;
    @EqualsAndHashCode.Exclude
    @Transient
    private int height;
    @EqualsAndHashCode.Exclude
    @Transient
    private int width;
    @EqualsAndHashCode.Exclude
    @Transient
    private char[][] boxPattern;

    public Box(String name, String form, char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.form = form.trim().replaceAll("[^~\\n\\s]", symbol + "");
        initTransientFields();
    }

    @PostLoad
    private void initTransientFields() {
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
}




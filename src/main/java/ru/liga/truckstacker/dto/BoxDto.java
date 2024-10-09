package ru.liga.truckstacker.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class BoxDto {

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String form;

    @NotBlank
    private final char symbol;

    @JsonCreator
    public BoxDto(@JsonProperty("name") String name,
                  @JsonProperty("form") String form,
                  @JsonProperty("symbol") char symbol) {
        this.name = name;
        this.symbol = symbol;
        this.form = form.trim().replaceAll("[^~\\n\\s]", symbol + "");
    }

    @Override
    public String toString() {
        return "\nBox{" +
                "name='" + name + '\'' +
                ", symbol=" + symbol +
                ", form=\'\n" + form + "\'}\n";
    }
}

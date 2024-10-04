package ru.liga.truckstacker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.BoxService;

import javax.validation.Valid;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BoxController {

    private final BoxService boxService;

    @ShellMethod(key = "get-all-boxes", value = "Получить все коробки.")
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @ShellMethod(key = "get-box-by-name", value = "Получить коробку по её имени.")
    public Box getBoxByName(@ShellOption(optOut = true) String boxName) {
        return boxService.getBoxByName(boxName);
    }

    @ShellMethod(key = "create-box", value = "Создать новую коробку.(перенос строки спомощью \\n")
    public String createBox(
            @ShellOption(optOut = true)
            String boxName,

            @ShellOption(optOut = true)
            String form,

            @ShellOption
            String symbol) {

        @Valid Box box = new Box(boxName, form, symbol.toCharArray()[0]);
        boxService.createBox(box);
        return "CREATED SUCCESS";
    }

    @ShellMethod(key = "update-box-by-name", value = "Обновить коробку, идентифицированную по её имени.")
    public String updateBoxByName(
            @ShellOption(optOut = true)
            String boxName,

            @ShellOption
            String form,

            @ShellOption
            String symbol) {

        @Valid Box box = new Box(boxName, form, symbol.toCharArray()[0]);
        boxService.updateBoxByName(box);
        return "UPDATED SUCCESS";
    }

    @ShellMethod(key = "delete-box-by-name", value = "Удалить коробку, идентифицированную по её имени.")
    public String deleteBoxByName(@ShellOption(optOut = true) String boxName) {
        boxService.deleteBoxByName(boxName);
        return "DELETED SUCCESS";
    }
}

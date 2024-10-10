package ru.liga.truckstacker.controller.telegram;

import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import lombok.RequiredArgsConstructor;
import ru.liga.truckstacker.config.telegram.TelegramMvcControllerProcessor;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.service.BoxService;

import javax.validation.Valid;
import java.util.List;

@BotController
@RequiredArgsConstructor
public class BoxBotController extends TelegramMvcControllerProcessor {
    private final BoxService boxService;

    @MessageRequest("/get-all-boxes")
    public List<BoxDto> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @MessageRequest("/get-box-by-name {boxName:[\\S]+}")
    public BoxDto getBoxByName(@BotPathVariable("boxName") String boxName) {
        return boxService.getBoxByName(boxName);
    }

    @MessageRequest("/create-box {boxName:[\\S]+}, {form:[\\S]+}, {symbol:[\\S]+}")
    public String createBox(@BotPathVariable("boxName") String boxName, @BotPathVariable("form") String form, @BotPathVariable("symbol") String symbol) {
        @Valid BoxDto box = new BoxDto(boxName, form, symbol);
        boxService.createBox(box);
        return "Successfully created";
    }

    @MessageRequest("/update-box-by-name {boxName:[\\S]+}, {form:[\\S]+}, {symbol:[\\S]+}")
    public String updateBoxByName(@BotPathVariable("boxName") String boxName, @BotPathVariable("form") String form, @BotPathVariable("symbol") String symbol) {
        @Valid BoxDto box = new BoxDto(boxName, form, symbol);
        boxService.updateBoxByName(box);
        return "Successfully updated";
    }

    @MessageRequest("/delete-box-by-name {boxName:[\\S]+}")
    public String deleteBoxByName(@BotPathVariable("boxName") String boxName) {
        boxService.deleteBoxByName(boxName);
        return "Successfully deleted";
    }
}

package ru.liga.truckstacker.controller.telegram;

import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import lombok.RequiredArgsConstructor;
import ru.liga.truckstacker.config.telegram.TelegramMvcControllerProcessor;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.service.TruckService;

import java.util.List;

@BotController
@RequiredArgsConstructor
public class TruckBotController extends TelegramMvcControllerProcessor {

    private final TruckService truckService;

    @MessageRequest("/stack {truckFileName:[\\S]+}, {boxFileName:[\\S]+}, {algorithm:[\\S]+}, {limitTrucksNumber:[\\S]+}, {truckSize:[\\S]+}, {boxNames:[\\S]+}")
    public List<TruckDto> stackBoxes(@BotPathVariable("truckFileName") String truckFileName,
                                     @BotPathVariable("boxFileName") String boxFileName,
                                     @BotPathVariable("algorithm") String algorithm,
                                     @BotPathVariable("limitTrucksNumber") Integer limitTrucksNumber,
                                     @BotPathVariable("truckSize") String truckSize,
                                     @BotPathVariable("boxNames") String boxNames) {
        StackingRequest stackingRequest = StackingRequest.builder()
                .trucksFileName(truckFileName)
                .boxesFileName(boxFileName)
                .typeAlgorithmName(algorithm)
                .limitNumberOfTrucks(limitTrucksNumber)
                .truckSize(truckSize)
                .boxNames(boxNames)
                .build();
        return truckService.stackBoxesAndSaveTrucks(stackingRequest);
    }

}
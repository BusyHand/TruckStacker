package ru.liga.truckstacker.controller.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.service.TruckService;

import java.util.List;

import static ru.liga.truckstacker.util.StackingRequestMessageHelper.*;

@ShellComponent
@RequiredArgsConstructor
public class TruckShellController {

    private final TruckService truckService;

    @ShellMethod(key = "stack", value = "Погрузка посылок")
    public List<TruckDto> stackBoxesAndSaveTrucks(
            @ShellOption(help = ALREADY_FILLED_TRUCKS_FILE_NAME_HELPER)
            String alreadyFilledTrucksFileName,

            @ShellOption(help = BOXES_FILE_NAME_HELPER)
            String boxesFileName,

            @ShellOption(help = TYPE_ALGORITHM_NAME_HELPER)
            String typeAlgorithmName,

            @ShellOption(help = TRUCK_SIZE_HELPER)
            String truckSize,

            @ShellOption(help = LIMIT_NUMBER_OF_TRUCKS_HELPER)
            int limitNumberOfTrucks,

            @ShellOption(help = BOX_NAMES_HELPER)
            String boxNames) {

        StackingRequest stackingRequest = StackingRequest.builder()
                .trucksFileName(alreadyFilledTrucksFileName)
                .boxesFileName(boxesFileName)
                .typeAlgorithmName(typeAlgorithmName)
                .limitNumberOfTrucks(limitNumberOfTrucks)
                .truckSize(truckSize)
                .boxNames(boxNames)
                .build();

        return truckService.stackBoxesAndSaveTrucks(stackingRequest);
    }
}

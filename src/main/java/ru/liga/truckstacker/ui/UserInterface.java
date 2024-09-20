package ru.liga.truckstacker.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.controller.ConsoleController;
import ru.liga.truckstacker.convertor.TrucksToStringConvertor;
import ru.liga.truckstacker.model.Truck;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInterface {

    private final ConsoleController controllerConsole;

    public void start() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter 'exit' to stop or press Enter to continue:");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the program...");
                break;
            }

            System.out.println("Write filename with trucks data (.json)");
            String TRUCKS_FILENAME = scanner.nextLine();

            System.out.println("Write filename with boxes data (.txt)");
            String BOXES_FILENAME = scanner.nextLine();

            System.out.println("Write type algorithm (balanced, burke, dummy)");
            String TYPE_ALGORITHM = scanner.nextLine();

            System.out.println("Write max size of truck usage");
            int MAX_SIZE = scanner.nextInt();

            log.info("User input received: Trucks file: {}, Boxes file: {}, Max size: {}, Algorithm type: {}",
                    TRUCKS_FILENAME, BOXES_FILENAME, MAX_SIZE, TYPE_ALGORITHM);

            List<Truck> trucks = controllerConsole.request(TRUCKS_FILENAME, BOXES_FILENAME, MAX_SIZE, TYPE_ALGORITHM);
            System.out.println(TrucksToStringConvertor.displayTrucks(trucks));
        }
    }
}

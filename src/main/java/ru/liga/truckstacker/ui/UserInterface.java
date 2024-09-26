    package ru.liga.truckstacker.ui;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Component;
    import ru.liga.truckstacker.controller.ConsoleController;
    import ru.liga.truckstacker.controller.StackingRequest;
    import ru.liga.truckstacker.convertor.Convertor;
    import ru.liga.truckstacker.model.Truck;

    import java.util.List;
    import java.util.Scanner;

    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class UserInterface {

        private final ConsoleController controllerConsole;
        private final Convertor<List<Truck>, String> truckStringConvertor;

        public void start() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter 'exit' to stop or press Enter to continue:");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the program...");
                    break;
                }
                StackingRequest.StackingRequestBuilder requestBuilder = StackingRequest.builder();
                System.out.println("Write filename with trucks data (.json)");
                String truckFileName = scanner.nextLine();

                System.out.println("Write filename with boxes data (.txt)");
                String boxesFileName = scanner.nextLine();

                System.out.println("Write type algorithm (balanced, burke, dummy)");
                String typeAlgorithmName = scanner.nextLine();

                System.out.println("Write max size of truck usage");
                int maxSizeNumberOfTrucks = scanner.nextInt();

                scanner.nextLine();
                System.out.flush();

                log.info("User input received: Trucks file: {}, Boxes file: {}, Max size: {}, Algorithm type: {}",
                        truckFileName, boxesFileName, maxSizeNumberOfTrucks, typeAlgorithmName);

                StackingRequest request = requestBuilder.trucksFileName(truckFileName)
                        .boxesFileName(boxesFileName)
                        .typeAlgorithmName(typeAlgorithmName)
                        .maxSizeNumberOfTrucks(maxSizeNumberOfTrucks)
                        .build();

                List<Truck> trucks = controllerConsole.stackBoxesAndSaveTrucks(request);
                System.out.println(truckStringConvertor.convert(trucks));
            }
        }
    }

package ru.liga.truckstacker.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.liga.truckstacker.ui.UserInterface;
@Component
@RequiredArgsConstructor
public class MainCommandLineRunner implements CommandLineRunner {

    private final UserInterface userInterface;

    @Override
    public void run(String... args) {
        userInterface.start();
    }
}

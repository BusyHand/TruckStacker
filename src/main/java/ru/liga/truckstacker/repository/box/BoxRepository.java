package ru.liga.truckstacker.repository.box;

import ru.liga.truckstacker.model.Box;

import java.util.List;

public interface BoxRepository {

    List<Box> getAllBoxes();

    Box getBoxByName(String boxName);

    List<Box> getBoxByNames(List<String> boxNames);

    void saveBox(Box box);

    Box updateBoxByName(Box box);

    void deleteBoxByName(String box);


}

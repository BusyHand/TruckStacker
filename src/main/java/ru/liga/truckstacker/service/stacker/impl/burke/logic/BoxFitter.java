package ru.liga.truckstacker.service.stacker.impl.burke.logic;

import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.stacker.impl.burke.model.CoordinatesInTruckArea;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatBox;
import ru.liga.truckstacker.service.stacker.impl.burke.model.HeightStatTruck;

public class BoxFitter {

    public void fitBoxByCoordinates(CoordinatesInTruckArea coordinates, HeightStatBox heightStatBox, HeightStatTruck heightStatTruck) {
        int[][] containerArea = heightStatTruck.getTruck().getTruckArea();
        Box box = heightStatBox.getBox();
        int[][] boxPattern = box.boxPattern();
        int containerY = coordinates.y();
        int boxY = 0;

        while (containerY - coordinates.y() < box.height() && containerY < containerArea.length) {
            int containerX = coordinates.x();
            int boxX = 0;

            while (containerX - coordinates.x() < box.width() && containerX < containerArea[0].length) {
                containerArea[containerY][containerX] = boxPattern[boxY][boxX];
                containerX++;
                boxX++;
            }
            containerY++;
            boxY++;
        }
    }
}

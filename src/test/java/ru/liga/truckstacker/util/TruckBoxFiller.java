package ru.liga.truckstacker.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.liga.truckstacker.config.GlobalSettings;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.BoxType;
import ru.liga.truckstacker.model.Truck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TruckBoxFiller {

    public static List<Truck> createEmptyTrucks(int count) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            trucks.add(new Truck()); // Assume a default constructor creates an empty truck
        }
        return trucks;
    }

    public static List<Truck> createFilledTrucks(int count) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Truck truck = new Truck();
            fillTruckWithBoxes(truck); // Fill truck with 5 boxes
            trucks.add(truck);
        }
        return trucks;
    }

    public static List<Truck> createFilledFullTrucks(int count) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Truck truck = new Truck();
            fillToFullTruckWithBoxes(truck); // Fill truck with 5 boxes
            trucks.add(truck);
        }
        return trucks;
    }

    public static List<Truck> createSomeBoxTrucks(int count) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Truck truck = new Truck();
            if (i % 2 == 0) {
                fillTruckWithBoxes(truck); // Fill half of the trucks
            }
            trucks.add(truck);
        }
        return trucks;
    }


    public static List<Box> createBoxes(int count) {
        List<Box> boxes = new ArrayList<>();
        // Using pre-defined box types from BoxType enum
        for (int i = 0; i < count; i++) {
            // For simplicity, let's say we rotate through available box types
            BoxType[] boxTypes = BoxType.values();
            // Adding boxes of various types, cycling through the defined BoxType values
            boxes.add(boxTypes[i % boxTypes.length].getBox());
        }
        return boxes;
    }

    private static void fillTruckWithBoxes(Truck truck) {
        Box box = BoxType.ONE.getBox();
        for (int i = 0; i < 6; i++) {
            truck.addBoxByCoordinates(0, i, box);
        }
    }

    private static void fillToFullTruckWithBoxes(Truck truck) {
        int[][] truckArea = new int[GlobalSettings.HEIGHT_TRUCK][GlobalSettings.WIDTH_TRUCK];
        for (int i = 0; i < GlobalSettings.HEIGHT_TRUCK; i++) {
            Arrays.fill(truckArea[i], 1);
        }
        truck.setTruckArea(truckArea);
    }
}

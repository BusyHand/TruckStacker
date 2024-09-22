package ru.liga.truckstacker.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.liga.truckstacker.convertor.BoxType;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.model.Truck;

import java.util.ArrayList;
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
            fillTruckWithBoxes(truck, 5); // Fill truck with 5 boxes
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
                fillTruckWithBoxes(truck, 3); // Fill half of the trucks
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

    private static void fillTruckWithBoxes(Truck truck, int count) {
        for (int i = 0; i < count; i++) {
            // Using a BoxType to retrieve a box, cycling through the box types
            Box box = BoxType.ONE.getBox(); // Assuming one type for all boxes for simplicity
            addBoxToTruck(truck, box);
        }
    }

    private static void fillToFullTruckWithBoxes(Truck truck) {
        int[][] truckArea = truck.getTruckArea();
        for (int i = 0; i < truckArea.length; i++) {
            for (int j = 0; j < truckArea[0].length; j++) {
                truckArea[i][j] = 1;
            }
        }
    }

    // Method to add a box to the truck's area
    private static void addBoxToTruck(Truck truck, Box box) {
        int[][] truckArea = truck.getTruckArea();
        // Naive logic to place box in the first empty position (if it fits)
        for (int y = 0; y < truckArea.length; y++) { // Start from the bottom
            for (int x = 0; x < truckArea[y].length; x++) {
                // Check if box can fit in the current position
                boolean canPlaceBox = true;
                for (int by = 0; by < box.height(); by++) {
                    if (y - by < 0 || truckArea[y - by][x] != 0) { // Check for space in the height
                        canPlaceBox = false;
                        break;
                    }
                }
                // If box fits, place it
                if (canPlaceBox) {
                    for (int by = 0; by < box.height(); by++) {
                        for (int bx = 0; bx < box.width(); bx++) {
                            truckArea[y - by][x + bx] = box.boxPattern()[by][bx]; // Place the box pattern
                        }
                    }
                    return; // Stop after placing the box
                }
            }
        }
    }
}

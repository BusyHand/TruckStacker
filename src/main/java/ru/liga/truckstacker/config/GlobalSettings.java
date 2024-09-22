package ru.liga.truckstacker.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds global configuration settings for the truck dimensions.
 * It provides static constants that define the height and width of the trucks used in the application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalSettings {


    public static final int HEIGHT_TRUCK = 6;


    public static final int WIDTH_TRUCK = 6;

}

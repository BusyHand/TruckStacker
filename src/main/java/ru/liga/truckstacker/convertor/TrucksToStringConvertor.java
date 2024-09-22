package ru.liga.truckstacker.convertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

import static ru.liga.truckstacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truckstacker.config.GlobalSettings.WIDTH_TRUCK;

@UtilityClass
public class TrucksToStringConvertor {

    public static String displayTrucks(List<Truck> truckList) {
        StringBuilder sb = new StringBuilder();
        int truckNumber = 1;

        for (Truck truck : truckList) {
            sb.append("Truck ").append(truckNumber++).append('\n');
            int[][] truckArea = truck.getTruckArea();

            for (int i = HEIGHT_TRUCK - 1; i > -1; i--) {
                sb.append('+');

                for (int j = 0; j < WIDTH_TRUCK; j++) {
                    int value = truckArea[i][j];
                    if (value == 0) {
                        sb.append(' ');
                    } else {
                        sb.append(value);
                    }
                }

                sb.append('+')
                        .append('\n');
            }

            for (int i = 0; i < WIDTH_TRUCK + 2; i++) {
                sb.append('+');
            }
            sb.append('\n')
                    .append('\n');
        }
        return sb.toString();
    }

}

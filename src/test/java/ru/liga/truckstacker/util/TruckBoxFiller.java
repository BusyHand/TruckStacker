package ru.liga.truckstacker.util;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;

@UtilityClass
public class TruckBoxFiller {
    @Test
    public void test() {
        String form = "\n\n\n~~~~\n\nasdf\nasd asd\n\n\n\n\n";
        char symbol = '1';
        System.out.println(form.trim().replaceAll("[^~\\n\\s]", symbol + ""));
    }
}

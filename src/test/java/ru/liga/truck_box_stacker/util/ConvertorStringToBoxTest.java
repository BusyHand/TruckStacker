package ru.liga.truck_box_stacker.util;

import org.junit.jupiter.api.Test;
import ru.liga.truck_box_stacker.model.Box;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.liga.truck_box_stacker.convertor.ConvertStringToBox.convert;

class ConvertorStringToBoxTest {

    @Test
    void convertor_string_to_box_test() {
        String one = "1\n";
        String two = "22\n";
        String three = "333\n";
        String four = "4444\n";
        String five = "55555\n";
        String six = "666\n666\n";
        String seven = "777\n7777\n";
        String eight = "8888\n8888\n";
        String nine = "999\n999\n999\n";

        Box convert1 = convert(one);
        Box convert2 = convert(two);
        Box convert3 = convert(three);
        Box convert4 = convert(four);
        Box convert5 = convert(five);
        Box convert6 = convert(six);
        Box convert7 = convert(seven);
        Box convert8 = convert(eight);
        Box convert9 = convert(nine);

        assertEquals(one, convert1.getStringInterpretation());
        assertEquals(two, convert2.getStringInterpretation());
        assertEquals(three, convert3.getStringInterpretation());
        assertEquals(four, convert4.getStringInterpretation());
        assertEquals(five, convert5.getStringInterpretation());
        assertEquals(six, convert6.getStringInterpretation());
        assertEquals(seven, convert7.getStringInterpretation());
        assertEquals(eight, convert8.getStringInterpretation());
        assertEquals(nine, convert9.getStringInterpretation());
    }
}
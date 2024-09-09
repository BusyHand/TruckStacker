package ru.liga.truck_box_stacker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.liga.truck_box_stacker.config.bpp.annotation.BoxInputFile;
import ru.liga.truck_box_stacker.model.Box;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.liga.truck_box_stacker.convertor.ConvertStringToBox.convert;

/**
 * Service for reading box specifications from a file and converting them
 * into a list of Box objects.
 *
 * This service utilizes a BufferedReader to read lines from a specified
 * input file, processes each line to accumulate strings representing
 * box specifications, and converts those strings into Box objects.
 */
@Slf4j
@Component
public class BoxReaderService {

    @BoxInputFile
    private Path boxInputFile;

    /**
     * Reads box specifications from the input file and returns a list
     * of Box objects.
     *
     * This method uses a BufferedReader to read the input file line by
     * line. It accumulates lines into a single string until a blank line
     * is encountered, which signifies the end of a box specification.
     * Once a blank line is hit, the accumulated string is converted
     * into a Box object and added to the result list.
     *
     * @return a List of Box objects created from the input file's
     *         specifications. If the input file is empty or only
     *         contains blank lines, an empty list is returned.
     */
    public List<Box> readAndGetBoxList() {
        List<Box> boxesResult = new ArrayList<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(boxInputFile, StandardCharsets.UTF_8)) {
            String line;
            AccumulateStrings accStr = new AccumulateStrings();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isBlank()) {
                    String strBox = accStr.collect();
                    Box box = convert(strBox);
                    boxesResult.add(box);
                } else {
                    accStr.appendLine(line);
                }
            }
        } catch (IOException e) {
            log.error("Input file was closed due to an error", e);
        }
        return boxesResult;
    }

    /**
     * Inner class for accumulating strings read from the input file.
     *
     * This class uses a StringBuilder to accumulate lines until a blank
     * line is encountered, at which point it collects the accumulated string
     * for conversion into a Box object.
     */
    private static class AccumulateStrings {
        StringBuilder stringBuilder = new StringBuilder();

        /**
         * Appends a line to the accumulated string.
         *
         * @param appendString the line to be added to the accumulated
         *                     string.
         */
        private void appendLine(String appendString) {
            stringBuilder.append(appendString).append("\n");
        }

        /**
         * Collects the accumulated lines into a single string and resets
         * the accumulator.
         *
         * @return the accumulated string containing all appended lines.
         */
        private String collect() {
            String result = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            return result;
        }
    }
}

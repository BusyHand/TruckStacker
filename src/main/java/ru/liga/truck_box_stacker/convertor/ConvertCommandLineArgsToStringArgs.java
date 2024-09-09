
package ru.liga.truck_box_stacker.convertor;

import lombok.extern.slf4j.Slf4j;
import ru.liga.truck_box_stacker.model.CommandLineArgs;
/**
 * Utility class for converting command line arguments to a format suitable
 * for Spring applications. This class contains methods that take in an array
 * of command line arguments and return them formatted as String arguments
 * that can be passed to a Spring application.
 */
@Slf4j
public class ConvertCommandLineArgsToStringArgs {
    /**
     * Converts the first command line argument to a Spring-compatible argument.
     *
     * This method generates an argument in the form of
     * "--PATH_INPUT_FILE=<first_argument>". It uses the
     * CommandLineArgs enum to get the name of the expected argument.
     *
     * @param args an array of command-line arguments. It is expected that
     *             this array contains at least one valid argument.
     * @return a String array containing a single formatted argument for Spring.
     * @throws IllegalArgumentException if the input array is null or empty.
     */
    public static String[] convertToSpringArgsValid(String[] args) {
        try {
            return new String[]{"--" + CommandLineArgs.PATH_INPUT_FILE.name() + "=" + args[0]};
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("input files not found");
            throw new RuntimeException();
        }
    }
}

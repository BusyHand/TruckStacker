package ru.liga.truckstacker.service.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.convertor.StringToBoxConvertor;
import ru.liga.truckstacker.model.Box;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BoxReaderConvertorService {

    public List<Box> readAndConvertToBoxList(String boxesFileName) {
        Path boxesInputPath = Path.of(boxesFileName);
        log.info("Starting to read boxes from file: {}", boxesFileName);
        try (BufferedReader bufferedReader = Files.newBufferedReader(boxesInputPath, StandardCharsets.UTF_8)) {
            List<Box> boxes = collectBoxList(bufferedReader);
            log.info("Successfully read {} boxes from file: {}", boxes.size(), boxesFileName);
            return boxes;
        } catch (IOException e) {
            log.error("Error reading input file: {}", boxesFileName, e);
            throw new RuntimeException("Input file was closed due to an error", e);
        }
    }

    private List<Box> collectBoxList(BufferedReader bufferedReader) throws IOException {
        List<Box> boxesResult = new ArrayList<>();
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            line = bufferedReader.readLine();
            if (line == null || line.isBlank()) {
                convert(boxesResult, stringBuilder);
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(line).append("\n");
                log.debug("Appending line: {}", line);
            }
        } while (line != null);
        return boxesResult;
    }

    private void convert(List<Box> boxesResult, StringBuilder stringBuilder) {
        String strBox = stringBuilder.toString();
        Box box = StringToBoxConvertor.convert(strBox);
        boxesResult.add(box);
        log.debug("Converted string to Box: {}", strBox);
    }
}

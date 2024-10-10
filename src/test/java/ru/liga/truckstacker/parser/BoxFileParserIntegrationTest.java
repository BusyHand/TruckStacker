package ru.liga.truckstacker.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.reader.FileReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BoxFileParserIntegrationTest {
    @Autowired
    private BoxFileParser boxFileParser;

    @Autowired
    private ObjectMapper objectMapper;

    Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        FileReader fileReader = new FileReader(); // Обеспечьте, чтобы FileReader мог принимать строки и возвращать их
        boxFileParser = new BoxFileParser(fileReader, objectMapper);
        tempFile = Files.createTempFile("test", ".json");
    }

    @AfterEach
    public void after() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testParseFromFileWithBlankFileName() {
        List<BoxDto> boxes = boxFileParser.parseBoxFile("   ");
        assertThat(boxes).isEmpty(); // Проверка, что список пустой
    }

    @Test
    public void testParseFromFile() throws IOException {
        // Создаем временный JSON файл для тестирования
        String json = """
                [
                  {
                    "name": "Штанга 1",
                    "form": "#      #\\n########\\n#      #",
                    "symbol": "#"
                  },
                  {
                    "name": "Штанга 1",
                    "form": "#      #\\n########\\n#      #",
                    "symbol": "#"
                  },
                  {
                    "name": "Штанга 1",
                    "form": "#      #\\n########\\n#      #",
                    "symbol": "#"
                  }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(3); // Проверка, что список содержит одну коробку
        assertThat(boxes.get(0).getName()).isEqualTo("Штанга 1");
        assertThat(boxes.get(0).getForm()).isEqualTo("#      #\n########\n#      #");
        assertThat(boxes.get(0).getSymbol()).isEqualTo("#");

        // Удаляем временный файл
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testParseMultipleBoxes() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 1",
                        "form": "# #\\n########\\n# #",
                        "symbol": "#"
                    },
                    {
                        "name": "Коробка 2",
                        "form": "## ##\\n##########\\n## ##",
                        "symbol": "*"
                    },
                    {
                        "name": "Коробка 3",
                        "form": " #  \\n#####\\n # ",
                        "symbol": "@"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(3);
        assertThat(boxes.get(0).getName()).isEqualTo("Коробка 1");
        assertThat(boxes.get(1).getName()).isEqualTo("Коробка 2");
        assertThat(boxes.get(2).getName()).isEqualTo("Коробка 3");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testEmptyFile() throws IOException {
        String json = "[]";
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).isEmpty();

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testParseBoxWithDifferentForm() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 4",
                        "form": "####\\n#  #\\n####",
                        "symbol": "+"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(1);
        assertThat(boxes.get(0).getForm()).isEqualTo("++++\n+  +\n++++");

        Files.deleteIfExists(tempFile);
    }

    @Test

    public void testInvalidJsonFormat() throws IOException {
        String json = "[{ name: 'Коробка 5' }]"; // некорректный JSON

        Path tempFile = null;
        tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        Path finalTempFile = tempFile;
        assertThrows(RuntimeException.class, () -> {
            boxFileParser.parseBoxFile(finalTempFile.toString());
        });
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testParseNestedForm() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 6",
                        "form": "  #  \\n #####\\n#     #",
                        "symbol": "%"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(1);
        assertThat(boxes.get(0).getSymbol()).isEqualTo("%");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testSymbolVariations() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 7",
                        "form": "##\\n##\\n##",
                        "symbol": "!"
                    },
                    {
                        "name": "Коробка 8",
                        "form": "++\\n++\\n++",
                        "symbol": "#"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(2);
        assertThat(boxes.get(0).getSymbol()).isEqualTo("!");
        assertThat(boxes.get(1).getSymbol()).isEqualTo("#");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testFormShapes() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 9",
                        "form": "###\\n # \\n###",
                        "symbol": "#"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(1);
        assertThat(boxes.get(0).getForm()).isEqualTo("###\n # \n###");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testDifferentCharacterTypes() throws IOException {
        String json = """
                [
                    {
                        "name": "Коробка 10",
                        "form": " * *\\n*****\\n * *",
                        "symbol": "0"
                    }
                ]
                """;
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(1);
        assertThat(boxes.get(0).getSymbol()).isEqualTo("0");

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testSpecialCharactersInName() throws IOException {
        String json = """
                [
                   {
                    "name": "Коробка @1",
                     "form": " * *\\n*****\\n * *",
                     "symbol": "0"
                   }
                ]""";
        Path tempFile = Files.createTempFile("test", ".json");
        Files.write(tempFile, json.getBytes());

        List<BoxDto> boxes = boxFileParser.parseBoxFile(tempFile.toString());

        assertThat(boxes).hasSize(1);
        assertThat(boxes.get(0).getName()).isEqualTo("Коробка @1");

        Files.deleteIfExists(tempFile);
    }

}

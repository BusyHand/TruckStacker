package ru.liga.truckstacker.service.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.convertor.BoxType;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.service.reader.BoxReaderConvertorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class BoxReaderConvertorServiceTest {
    @Autowired
    private BoxReaderConvertorService boxReaderConvertorService;

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("boxes", ".txt");
    }

    @Test
    void testReadAndConvertValidBox() throws IOException {
        Box box = BoxType.TWO.getBox();
        Files.writeString(tempFile, box.representation());

        List<Box> boxList = boxReaderConvertorService.readAndConvertToBoxList(tempFile.toString());

        assertThat(boxList).hasSize(1);
        assertThat(boxList.get(0)).isEqualTo(box);
    }

    @Test
    void testReadAndConvertMultipleBoxes() throws IOException {
        // Запись нескольких валидных строк для боксов в файл
        Box boxTwo = BoxType.TWO.getBox();
        Box boxThree = BoxType.THREE.getBox();
        Box boxFour = BoxType.FOUR.getBox();
        String multipleBoxesContent = boxTwo.representation() + "\n" + boxThree.representation() + "\n" + boxFour.representation();
        Files.writeString(tempFile, multipleBoxesContent);

        List<Box> boxList = boxReaderConvertorService.readAndConvertToBoxList(tempFile.toString());

        assertThat(boxList).hasSize(3);
        assertThat(boxList.get(0).representation()).isEqualTo(boxTwo.representation());
        assertThat(boxList.get(1).representation()).isEqualTo(boxThree.representation());
        assertThat(boxList.get(2).representation()).isEqualTo(boxFour.representation());
    }

    @Test
    void testReadAndConvertWithEmptyLine() throws IOException {
        Box boxTwo = BoxType.TWO.getBox();
        Box boxThree = BoxType.THREE.getBox();
        Box boxSeven = BoxType.SEVEN.getBox();
        String multipleBoxesContent = boxTwo.representation() + "\n" + boxThree.representation() + "\n" + boxSeven.representation();
        Files.writeString(tempFile, multipleBoxesContent);

        List<Box> boxList = boxReaderConvertorService.readAndConvertToBoxList(tempFile.toString());

        assertThat(boxList).hasSize(3);
        assertThat(boxList.get(0).representation()).isEqualTo(boxTwo.representation());
        assertThat(boxList.get(1).representation()).isEqualTo(boxThree.representation());
        assertThat(boxList.get(2).representation()).isEqualTo(boxSeven.representation());
    }

}

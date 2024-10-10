package ru.liga.truckstacker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.liga.truckstacker.entity.FileEntity;
import ru.liga.truckstacker.enums.FileType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class FileEntityRepositoryTest {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @BeforeEach
    void setUp() {
        FileEntity fileEntity1 = new FileEntity("trucks.json", FileType.TRUCK_FILE);
        FileEntity fileEntity2 = new FileEntity("boxes.json", FileType.BOX_FILE);
        FileEntity fileEntity3 = new FileEntity("data.json", FileType.TRUCK_FILE);
        fileEntityRepository.save(fileEntity1);
        fileEntityRepository.save(fileEntity2);
        fileEntityRepository.save(fileEntity3);
    }

    @Test
    void testFindFileEntitiesByFileType() {
        // Act
        List<FileEntity> truckFiles = fileEntityRepository.findFileEntitiesByFileType(FileType.TRUCK_FILE);

        // Assert
        assertThat(truckFiles).hasSize(2);
        assertThat(truckFiles).extracting(FileEntity::getFileName).containsExactlyInAnyOrder("trucks.json", "data.json");
    }

    @Test
    void testFindAllFiles() {
        // Act
        List<FileEntity> allFiles = fileEntityRepository.findAll();

        // Assert
        assertThat(allFiles).hasSize(3);
    }

    @Test
    void testDeleteNonExistingFileEntity() {
        // Act & Assert
        assertThatThrownBy(() -> fileEntityRepository.deleteById(999L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}

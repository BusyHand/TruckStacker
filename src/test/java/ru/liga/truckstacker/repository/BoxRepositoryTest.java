package ru.liga.truckstacker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.liga.truckstacker.entity.Box;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BoxRepositoryTest {

    @Autowired
    private BoxRepository boxRepository;

    @BeforeEach
    void setUp() {
        Box boxA = new Box("BoxA", "FormA", 'A');
        Box boxB = new Box("BoxB", "FormB", 'B');
        boxRepository.save(boxA);
        boxRepository.save(boxB);
    }

    @Test
    void testFindAll() {
        // Act
        List<Box> boxes = boxRepository.findAll();

        // Assert
        assertThat(boxes).hasSize(2);
        assertThat(boxes).extracting(Box::getName).containsExactlyInAnyOrder("BoxA", "BoxB");
    }

    @Test
    void testUpdateBox() {
        // Arrange
        Box boxB = boxRepository.findById("BoxB").orElse(null);
        assertThat(boxB).isNotNull();
        boxB.setForm("UpdatedFormB");

        // Act
        boxRepository.save(boxB);
        Box updatedBoxB = boxRepository.findById("BoxB").orElse(null);

        // Assert
        assertThat(updatedBoxB).isNotNull();
        assertThat(updatedBoxB.getForm()).isEqualTo("UpdatedFormB");
    }

    @Test
    void testDeleteBox() {
        // Act
        boxRepository.deleteById("BoxA");

        // Assert
        assertThat(boxRepository.findById("BoxA")).isEmpty();
    }

    @Test
    void testDeleteNonExistingBox() {
        // Act & Assert
        assertThatThrownBy(() -> boxRepository.deleteById("NonExistent"))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void testFindByName() {
        // Act
        Box foundBox = boxRepository.findById("BoxA").orElse(null);

        // Assert
        assertThat(foundBox).isNotNull();
        assertThat(foundBox.getName()).isEqualTo("BoxA");
    }
}

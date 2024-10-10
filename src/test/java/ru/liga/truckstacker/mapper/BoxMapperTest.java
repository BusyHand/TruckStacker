package ru.liga.truckstacker.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.entity.Box;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoxMapperTest {

    private final BoxMapper boxMapper = Mappers.getMapper(BoxMapper.class);

    @Test
    void testToEntity() {
        // Arrange
        BoxDto boxDto = new BoxDto("BoxA", "formA", "A");

        // Act
        Box box = boxMapper.toEntity(boxDto);

        // Assert
        assertThat(box).isNotNull();
        assertThat(box.getName()).isEqualTo("BoxA");
        assertThat(box.getForm()).isEqualTo("AAAAA");
        assertThat(box.getSymbol()).isEqualTo('A');
    }

    @Test
    void testToDto() {
        // Arrange
        Box box = new Box("BoxA", "formA", 'A');

        // Act
        BoxDto boxDto = boxMapper.toDto(box);

        // Assert
        assertThat(boxDto).isNotNull();
        assertThat(boxDto.getName()).isEqualTo("BoxA");
        assertThat(boxDto.getForm()).isEqualTo("AAAAA");
        assertThat(boxDto.getSymbol()).isEqualTo("A");
    }

    @Test
    void testToDtoList() {
        // Arrange
        Box box1 = new Box("BoxA", "formA", 'A');
        Box box2 = new Box("BoxB", "formB", 'B');
        List<Box> boxes = List.of(box1, box2);

        // Act
        List<BoxDto> boxDtos = boxMapper.toDto(boxes);

        // Assert
        assertThat(boxDtos).hasSize(2);
        assertThat(boxDtos).extracting(BoxDto::getName).containsExactly("BoxA", "BoxB");
    }

    @Test
    void testToEntityList() {
        // Arrange
        BoxDto boxDto1 = new BoxDto("BoxA", "formA", "A");
        BoxDto boxDto2 = new BoxDto("BoxB", "formB", "B");
        List<BoxDto> boxDtos = List.of(boxDto1, boxDto2);

        // Act
        List<Box> boxes = boxMapper.toEntity(boxDtos);

        // Assert
        assertThat(boxes).hasSize(2);
        assertThat(boxes).extracting(Box::getName).containsExactly("BoxA", "BoxB");
    }
}

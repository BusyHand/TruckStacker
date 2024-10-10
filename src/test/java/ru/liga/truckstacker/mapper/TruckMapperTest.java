package ru.liga.truckstacker.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TruckMapperTest {

    private final TruckMapper truckMapper = Mappers.getMapper(TruckMapper.class);

    @Test
    void testToDto() {
        // Arrange
        Truck truck = new Truck(2, 3);
        Map<Box, Integer> boxesInTruck = new HashMap<>();
        Box boxA = new Box("BoxA", "formA", 'A');
        boxesInTruck.put(boxA, 1);
        truck.setBoxesInTruck(boxesInTruck);

        // Act
        TruckDto truckDto = truckMapper.toDto(truck);

        // Assert
        assertThat(truckDto).isNotNull();
        assertThat(truckDto.getTruckArea()).isNotNull();
        assertThat(truckDto.getBoxesInTruck()).containsEntry(boxA, 1);
    }

    @Test
    void testToEntity() {
        // Arrange
        TruckDto truckDto = new TruckDto();
        truckDto.setTruckArea(new char[][]{{'A', 'A'}, {'B', 'B'}});
        Map<Box, Integer> boxesInTruck = new HashMap<>();
        Box boxA = new Box("BoxA", "formA", 'A');
        boxesInTruck.put(boxA, 1);
        truckDto.setBoxesInTruck(boxesInTruck);

        // Act
        Truck truck = truckMapper.toEntity(truckDto);

        // Assert
        assertThat(truck).isNotNull();
        assertThat(truck.getBoxesInTruck()).containsEntry(boxA, 1);
    }

    @Test
    void testToDtoList() {
        // Arrange
        Truck truck1 = new Truck(2, 3);
        Truck truck2 = new Truck(4, 5);
        List<Truck> trucks = List.of(truck1, truck2);

        // Act
        List<TruckDto> truckDtos = truckMapper.toDto(trucks);

        // Assert
        assertThat(truckDtos).hasSize(2);
    }

    @Test
    void testToEntityList() {
        // Arrange
        TruckDto truckDto1 = new TruckDto();
        TruckDto truckDto2 = new TruckDto();
        List<TruckDto> truckDtos = List.of(truckDto1, truckDto2);

        // Act
        List<Truck> trucks = truckMapper.toEntity(truckDtos);

        // Assert
        assertThat(trucks).hasSize(2);
    }
}

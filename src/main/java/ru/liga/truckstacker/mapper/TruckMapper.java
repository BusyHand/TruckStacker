package ru.liga.truckstacker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.entity.Truck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    @Mapping(target = "boxesInTruck", expression = "java(getBoxesInTruck(entity))")
    TruckDto toDto(Truck entity);

    default Map<Box, Integer> getBoxesInTruck(Truck entity) {
        return entity.getBoxesInTruck() != null ? new HashMap<>(entity.getBoxesInTruck()) : new HashMap<>();
    }

    Truck toEntity(TruckDto dto);

    List<TruckDto> toDto(List<Truck> entitiesList);

    List<Truck> toEntity(List<TruckDto> dtoList);
}

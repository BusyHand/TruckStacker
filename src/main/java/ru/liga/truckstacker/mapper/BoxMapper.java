package ru.liga.truckstacker.mapper;

import org.mapstruct.Mapper;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.entity.Box;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoxMapper {

    default Box toEntity(BoxDto boxDto) {
        return new Box(boxDto.getName(), boxDto.getForm(), boxDto.getSymbol().toCharArray()[0]);
    }

    BoxDto toDto(Box entity);

    List<BoxDto> toDto(List<Box> entitiesList);

    List<Box> toEntity(List<BoxDto> dtoList);
}

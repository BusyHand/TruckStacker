package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.mapper.BoxMapper;
import ru.liga.truckstacker.repository.BoxRepository;

import java.util.List;
import java.util.Optional;
/**
 * Ну думаю очевидный класс :)
 */
@Service
@RequiredArgsConstructor
public class BoxService {

    private final BoxRepository boxRepository;

    private final BoxMapper boxMapper;

    public List<BoxDto> getAllBoxes() {
        List<Box> boxes = boxRepository.findAll();
        return boxMapper.toDto(boxes);
    }

    public BoxDto getBoxByName(String boxName) {
        Optional<Box> optionalBox = boxRepository.findById(boxName);
        Box box = optionalBox
                .orElseThrow(() -> new BoxNotFoundException("Коробки с таким именим, не найдено :" + boxName));
        return boxMapper.toDto(box);
    }

    public BoxDto createBox(BoxDto boxDto) {
        Box box = boxMapper.toEntity(boxDto);
        Box savedBox = boxRepository.save(box);
        return  boxMapper.toDto(savedBox);
    }

    public BoxDto updateBoxByName(BoxDto boxDto) {
        Box box = boxMapper.toEntity(boxDto);
        boxRepository.findById(box.getName())
                .orElseThrow(() -> new BoxNotFoundException("Коробки с таким именим, не найдено :" + box.getName()));
        boxRepository.save(box);
        return boxMapper.toDto(box);
    }

    public void deleteBoxByName(String boxName) {
        boxRepository.deleteById(boxName);
    }
}

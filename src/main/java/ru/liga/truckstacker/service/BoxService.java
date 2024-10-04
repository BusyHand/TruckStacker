package ru.liga.truckstacker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.repository.box.BoxRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxService {

    private final BoxRepository boxRepository;

    @Cacheable(value = "boxes")
    public List<Box> getAllBoxes() {
        return boxRepository.getAllBoxes();
    }

    @Cacheable(value = "box", key = "#boxName")
    public Box getBoxByName(String boxName) {
        return boxRepository.getBoxByName(boxName);
    }

    @CacheEvict(value = {"boxes", "box"}, allEntries = true)
    public void createBox(Box box) {
        boxRepository.saveBox(box);
    }

    @CachePut(value = "box", key = "#box.name")
    public Box updateBoxByName(Box box) {
        return boxRepository.updateBoxByName(box);
    }

    @CacheEvict(value = {"boxes", "box"}, key = "#boxName")
    public void deleteBoxByName(String boxName) {
        boxRepository.deleteBoxByName(boxName);
    }
}

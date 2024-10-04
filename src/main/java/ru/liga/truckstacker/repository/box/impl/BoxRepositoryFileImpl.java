package ru.liga.truckstacker.repository.box.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.liga.truckstacker.convertor.BoxDaoConverter;
import ru.liga.truckstacker.exception.BoxNotFoundException;
import ru.liga.truckstacker.exception.BoxWithThisNameAlreadyExistException;
import ru.liga.truckstacker.model.Box;
import ru.liga.truckstacker.repository.box.BoxRepository;
import ru.liga.truckstacker.repository.box.dao.BoxFileDbDao;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;


@Repository
@RequiredArgsConstructor
public class BoxRepositoryFileImpl implements BoxRepository {

    private final BoxFileDbDao boxFileDbDao;

    @Override
    public List<Box> getAllBoxes() {
        return boxFileDbDao.readAround(lines ->
                lines.map(BoxDaoConverter::convertLineToBox)
                        .toList()
        );
    }

    @Override
    public Box getBoxByName(String boxName) {
        return boxFileDbDao.readAround(lines ->
                lines.map(BoxDaoConverter::convertLineToBox)
                        .filter(box -> box.getName().equals(boxName))
                        .findFirst()
                        .orElseThrow(() -> new BoxNotFoundException(""))
        );
    }

    @Override
    public List<Box> getBoxByNames(List<String> boxNames) {
        Map<String, Long> boxCount = boxNames.stream()
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()));
        return boxFileDbDao.readAround(lines -> lines
                .map(BoxDaoConverter::convertLineToBox)
                .filter(box -> boxCount.containsKey(box.getName()))
                .flatMap(box -> LongStream.range(0, boxCount.get(box.getName()))
                        .mapToObj(i -> box)
                )
                .toList());
    }

    @Override
    public void saveBox(Box box) {
        if (isExistInDb(box)) {
            throw new BoxWithThisNameAlreadyExistException("");
        }
        boxFileDbDao.writeAround(() -> BoxDaoConverter.convertBoxToLine(box));
    }

    @Override
    public Box updateBoxByName(Box box) {
        if (!isExistInDb(box)) {
            throw new BoxNotFoundException("");
        }
        boxFileDbDao.readAndWriteAround((lines, writer) -> {
            lines.map(BoxDaoConverter::convertLineToBox)
                    .filter(dbBox -> !dbBox.getName().equals(box.getName()))
                    .map(BoxDaoConverter::convertBoxToLine)
                    .forEach(writer::println);
            writer.println(BoxDaoConverter.convertBoxToLine(box));
        });
        return box;
    }

    private boolean isExistInDb(Box box) {
        return boxFileDbDao.readAround(lines ->
                lines.map(BoxDaoConverter::convertLineToBox)
                        .anyMatch(dbBox -> dbBox.getName().equals(box.getName()))
        );
    }

    @Override
    public void deleteBoxByName(String boxName) {
        boxFileDbDao.readAndWriteAround((lines, writer) -> {
            lines.map(BoxDaoConverter::convertLineToBox)
                    .filter(dbBox -> !dbBox.getName().equals(boxName))
                    .map(BoxDaoConverter::convertBoxToLine)
                    .forEach(writer::println);
        });
    }
}

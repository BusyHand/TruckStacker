package ru.liga.truckstacker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.truckstacker.entity.FileEntity;
import ru.liga.truckstacker.enums.FileType;

import java.util.List;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findFileEntitiesByFileType(FileType fileType);
}

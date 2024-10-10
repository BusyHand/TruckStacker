package ru.liga.truckstacker.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.liga.truckstacker.enums.FileType;

import javax.persistence.*;


/**
 * Класс для сохранения в базе данных имя файла, и его тип (грузовики или коробки)
 * Нужен чтобы при повторном поднятие приложения осталось информация о файлах которые передавал пользователь
 *
 */
@Entity
@Table(name = "file_entities")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    public FileEntity(String fileName, FileType fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }
}

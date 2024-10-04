package ru.liga.truckstacker.repository.box.dao;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Репозиторий для работы с файлами коробок.
 */
@Repository
public class BoxFileDbDao {

    private static final Path db = Path.of("db", "boxes", "boxes.txt");
    private final ReentrantLock lock = new ReentrantLock();

    private void createFileIfNotExist() {
        createFileIfNotExist(db);
    }

    private void createFileIfNotExist(Path path) {
        try {
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Читает данные из файла, обрабатывая их с помощью заданной функции.
     *
     * @param function Функция, принимающая поток строк и возвращающая значение.
     * @param <T>      Тип возвращаемого значения.
     * @return Результат выполнения функции.
     */
    public <T> T readAround(Function<Stream<String>, T> function) {
        lock.lock();
        createFileIfNotExist();
        try (var lines = Files.lines(db)) {
            return function.apply(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Записывает строку в файл, используя заданную функцию.
     *
     * @param function Функция, возвращающая строку для записи.
     */
    public void writeAround(Supplier<String> function) {
        lock.lock();
        createFileIfNotExist();
        try (var printWriter = new PrintWriter(Files.newBufferedWriter(db, StandardCharsets.UTF_8, StandardOpenOption.APPEND))) {
            String stringBox = function.get();
            printWriter.println(stringBox);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Читает данные из файла и записывает их в новый файл, обрабатывая с помощью заданной функции.
     *
     * @param function Функция, принимающая поток строк и объект PrintWriter для записи.
     */
    public void readAndWriteAround(BiConsumer<Stream<String>, PrintWriter> function) {
        lock.lock();
        Path tempDb = Path.of("db", "boxes", "temp-boxes.txt");
        createFileIfNotExist(tempDb);
        try (var lines = Files.lines(db);
             var printWriter = new PrintWriter(Files.newBufferedWriter(tempDb, StandardCharsets.UTF_8))) {
            function.accept(lines, printWriter);
            Files.delete(db);
            Files.move(tempDb, db);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}

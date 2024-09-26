package ru.liga.truckstacker.model;

/**
 * Объект, представляющий коробку, имеющую заданные высоту, ширину, отображение и шаблон.
 * @param height Высота коробки.
 * @param width Ширина коробки.
 * @param representation Строковое представление коробки.
 * @param boxPattern Шаблон коробки, представляющий ее структуру.
 */
public record Box(
        int height,
        int width,
        String representation,
        int[][] boxPattern) {

    @Override
    public String toString() {
        return representation.substring(0, representation.length() - 1);
    }
}




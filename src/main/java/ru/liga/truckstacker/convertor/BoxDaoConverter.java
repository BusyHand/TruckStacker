package ru.liga.truckstacker.convertor;

import lombok.experimental.UtilityClass;
import ru.liga.truckstacker.entity.Box;

/**
 * Утилитный класс для конвертации между строковым представлением коробки и объектом Box.
 */
@UtilityClass
public class BoxDaoConverter {

    /**
     * Конвертирует строковое представление коробки в объект Box.
     *
     * @param daoValidBox Строка, представляющая валидную коробку в формате данных.
     * @return Объект Box, созданный на основе переданной строки.
     */
    public Box convertLineToBox(String daoValidBox) {
        String boxString = daoValidBox.replace("~", "\n");
        boxString = boxString.replace("[", " ");
        String[] splitedBox = boxString.split("\t");
        return new Box(splitedBox[0], splitedBox[1], splitedBox[2].toCharArray()[0]);
    }

    /**
     * Конвертирует объект Box в строковое представление.
     *
     * @param box Объект Box, который необходимо конвертировать.
     * @return Строка, представляющая объект Box в формате данных.
     */
    public String convertBoxToLine(Box box) {
        String boxString = box.getName() + "\t" + box.getForm() + "\t" + box.getSymbol();
        boxString = boxString.replace(" ", "[");
        return boxString.replace("\n", "~");
    }
}

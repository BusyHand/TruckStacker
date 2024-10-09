package ru.liga.truckstacker.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StackingRequestMessageHelper {
    public static final String ALREADY_FILLED_TRUCKS_FILE_NAME_HELPER = "Имя файла с уже загруженными грузовиками (.json)";
    public static final String BOXES_FILE_NAME_HELPER = "Имя файла с коробками (.json)";
    public static final String TYPE_ALGORITHM_NAME_HELPER = "Имя алгоритма (burke, balanced, dummy)";
    public static final String LIMIT_NUMBER_OF_TRUCKS_HELPER = "Максимальное количество грузовиков для использования";
    public static final String TRUCK_SIZE_HELPER = """
            Размер грузовика в формате HEIGHTxWIDTH (например, 6x6, 5x6, 3x3 - в этом случае максимальное использования грузовиков будет равно количеству заданных размеров)
            Если задан только один размер то ограничения по использованию будет браться из параметра (limitNumberOfTrucks)
            Если не задан не один аргумент то по умолчанию для всех грузовиков размер 6x6""";
    public static final String BOX_NAMES_HELPER = "Названия уже сохраненых коробок, которые нужно упоковать. Корректный ввод через \",\" без пробелов. Пример: Штанга,Кот в коробке";
}

package ru.liga.truckstacker.service.boxgetter;

import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.entity.Box;

import java.util.List;

/**
 * Интерфейс для получения коробок различными способами:
 * из файла, предоставленного пользователем,
 * по имени коробки, введенному пользователем,
 * а также распаковка грузовиков, пришедших в файле от пользователя.
 * <p>
 * Данный интерфейс реализован в рамках шаблона проектирования
 * "Цепочка ответственности" (Chain of Responsibility), что позволяет
 * легко добавлять новые способы получения коробок в будущем.
 */
public interface StackingRequestBoxesGetter {

    /**
     * Получает список коробок на основе предоставленного запроса укладки.
     *
     * @param stackingRequest Запрос на укладку, содержащий информацию о коробках.
     * @return List<Box> Список коробок, полученных из запроса на укладку.
     */

    List<Box> getBoxes(StackingRequest stackingRequest);

}

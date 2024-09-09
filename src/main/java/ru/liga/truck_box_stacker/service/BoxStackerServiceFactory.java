package ru.liga.truck_box_stacker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.liga.truck_box_stacker.config.bpp.annotation.AutowiredTypeAlgorithmToBeanName;
import ru.liga.truck_box_stacker.model.TypeAlgorithm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

/**
 * A factory class responsible for providing the appropriate
 * BoxStackerService implementation based on the size of the data
 * to be processed and the available algorithms.
 *
 * This factory uses dependency injection to manage different
 * implementations of BoxStackerService, mapping them to specific
 * algorithm types. It decides which algorithm to use based on
 * the size of the input data.
 */
@Component
@RequiredArgsConstructor
public class BoxStackerServiceFactory {

    private final Map<String, BoxStackerService> beanNamesToAlgorithm;

    @AutowiredTypeAlgorithmToBeanName
    private Map<TypeAlgorithm, String> typeAlgorithmToBeanNames;

    /**
     * Retrieves the appropriate BoxStackerService based on the size
     * of the input data. This method determines the suitable algorithm
     * by checking the maximum size limits associated with each algorithm type.
     *
     * The algorithms are sorted based on their allowable maximum input size.
     * The first algorithm that can handle the given size is chosen.
     *
     * @param size the size of the data to be processed.
     * @return the BoxStackerService that can best handle the specified size.
     */
    public BoxStackerService getBoxStackerServiceBySizeOfData(int size) {
        TypeAlgorithm[] typeAlgorithms = TypeAlgorithm.values();
        Arrays.sort(typeAlgorithms, Comparator.comparing(TypeAlgorithm::getMaxSizeOfDataToUseAlgorithm));
        TypeAlgorithm lastType = typeAlgorithms[0];

        for (TypeAlgorithm typeAlgorithm : typeAlgorithms) {
            int maxSize = typeAlgorithm.getMaxSizeOfDataToUseAlgorithm();
            if (size <= maxSize) {
                lastType = typeAlgorithm;
                break;
            }
        }
        return getBoxStackerServiceByType(lastType);
    }

    /**
     * Retrieves the BoxStackerService implementation based on the specified
     * TypeAlgorithm.
     *
     * @param typeAlgorithm the TypeAlgorithm that indicates which stacking
     *                      algorithm to use.
     * @return the corresponding BoxStackerService implementation.
     */
    public BoxStackerService getBoxStackerServiceByType(TypeAlgorithm typeAlgorithm) {
        String beanName = typeAlgorithmToBeanNames.get(typeAlgorithm);
        return beanNamesToAlgorithm.get(beanName);
    }
}

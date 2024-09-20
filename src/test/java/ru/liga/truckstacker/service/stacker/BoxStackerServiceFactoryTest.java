package ru.liga.truckstacker.service.stacker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.config.bpp.TypeAlgorithm;
import ru.liga.truckstacker.repository.TruckRepository;
import ru.liga.truckstacker.service.stacker.impl.burke.BurkeBoxStackerServiceImpl;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class BoxStackerServiceFactoryTest {

    @Autowired
    private BoxStackerServiceFactory boxStackerServiceFactory;

    @MockBean
    private TruckRepository truckRepository;

    @MockBean
    private BurkeBoxStackerServiceImpl burkeBoxStackerServiceImpl;

    @BeforeEach
    void setUp() throws Exception {
        // Set up the typeAlgorithmToBeanNames map using reflection
        Field field = BoxStackerServiceFactory.class.getDeclaredField("typeAlgorithmToBeanNames");
        field.setAccessible(true);

        Map<TypeAlgorithm, String> typeAlgorithmMap = new HashMap<>();
        typeAlgorithmMap.put(TypeAlgorithm.BURKE, "burkeBoxStackerServiceImpl");
        field.set(boxStackerServiceFactory, typeAlgorithmMap);
    }

    @Test
    void testRepositorySaveMethodCalledWithBurkeAlgorithm() {
        // Arrange
        when(burkeBoxStackerServiceImpl.stackBoxes(Collections.EMPTY_LIST, 1, Collections.EMPTY_LIST))
                .thenReturn(Collections.EMPTY_LIST);
        // Act
        boxStackerServiceFactory.stackBoxesAndSaveTrucks(Collections.EMPTY_LIST, 1, Collections.EMPTY_LIST, "burke");

        // Assert
        verify(burkeBoxStackerServiceImpl).stackBoxes(Collections.EMPTY_LIST, 1, Collections.EMPTY_LIST); // Ensure stackBoxes is called
        verify(truckRepository).save(Collections.EMPTY_LIST); // Ensure save is called on the repository
    }

}

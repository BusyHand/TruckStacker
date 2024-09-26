package ru.liga.truckstacker.service.stacker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.config.TestMockStackerServiceTest;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.repository.TruckRepository;
import ru.liga.truckstacker.service.stacker.strategy.burke.BurkeBoxStackerStrategy;
import ru.liga.truckstacker.controller.BoxStackingRequest;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import({TestMockBeanConfig.class, TestMockStackerServiceTest.class})
public class BoxStackerStrategyStrategyTest {

    @Autowired
    private BoxStackerStrategyService boxStackerStrategyService;

    @Autowired
    private BurkeBoxStackerStrategy burkeBoxStackerStrategy;
    @MockBean
    private TruckRepository truckRepository;

    @Test
    void testRepositorySaveMethodCalledWithBurkeAlgorithm() {
        List<Truck> mockTrucks = Collections.singletonList(new Truck());
        when(burkeBoxStackerStrategy.stackBoxes(Collections.EMPTY_LIST, 1, Collections.EMPTY_LIST))
                .thenReturn(mockTrucks);

        BoxStackingRequest boxStackingRequest = BoxStackingRequest.builder()
                .trucks(Collections.EMPTY_LIST)
                .boxes(Collections.EMPTY_LIST)
                .maxSizeNumberOfTrucks(1)
                .typeAlgorithmName("burke")
                .build();

        boxStackerStrategyService.stackBoxesAndSaveTrucks(boxStackingRequest);

        verify(burkeBoxStackerStrategy).stackBoxes(Collections.EMPTY_LIST, 1, Collections.EMPTY_LIST);
        verify(truckRepository).save(mockTrucks);
    }


}

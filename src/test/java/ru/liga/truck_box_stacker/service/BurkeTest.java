package ru.liga.truck_box_stacker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.BoxType;
import ru.liga.truck_box_stacker.model.TruckList;
import ru.liga.truck_box_stacker.service.impl.burke.BurkeBoxStackerServiceImpl;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BurkeTest {
    @InjectMocks
    private BurkeBoxStackerServiceImpl burkeBoxStackerService;

    public BurkeTest() {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStackBoxes_ReturnsOneTruck_ForSingleBox() {
        Box box = BoxType.ONE.getBox();
        TruckList truckList = burkeBoxStackerService.stackBoxes(Collections.singletonList(box));
        assertEquals(1, truckList.getTruckList().size(), "Expected one truck to be returned.");
    }
}
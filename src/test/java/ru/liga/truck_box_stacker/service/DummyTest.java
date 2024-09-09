package ru.liga.truck_box_stacker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.liga.truck_box_stacker.model.Box;
import ru.liga.truck_box_stacker.model.BoxType;
import ru.liga.truck_box_stacker.model.TruckList;
import ru.liga.truck_box_stacker.service.impl.dummy.DummyBoxStackerServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyTest {
    @InjectMocks
    private DummyBoxStackerServiceImpl dummyBoxStackerServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStackBoxes_ReturnsTwoTruck_ForTwoBox() {
        Box box = BoxType.ONE.getBox();
        TruckList truckList = dummyBoxStackerServiceImpl.stackBoxes(new ArrayList<>(List.of(box, box)));
        assertEquals(2, truckList.getTruckList().size(), "Expected one truck to be returned.");
    }
}
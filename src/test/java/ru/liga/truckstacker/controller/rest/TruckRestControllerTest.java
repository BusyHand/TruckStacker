package ru.liga.truckstacker.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.entity.Box;
import ru.liga.truckstacker.service.TruckService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TruckRestController.class)
public class TruckRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TruckService truckService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testStackBoxesAndSaveTrucks() throws Exception {
        // Создание mock-данных для Box
        Box boxA = new Box();
        boxA.setName("один");
        boxA.setForm("1");
        boxA.setSymbol('1');

        // Создание mock-данных для TruckDto
        TruckDto mockTruckDto = new TruckDto();
        mockTruckDto.setTruckArea(new char[][]{{'#', '#'}, {'#', '#'}});

        // Инициализация Map<Box, Integer>
        Map<Box, Integer> boxesInTruck = new HashMap<>();
        boxesInTruck.put(boxA, 2);
        mockTruckDto.setBoxesInTruck(boxesInTruck);

        List<TruckDto> mockResponse = Collections.singletonList(mockTruckDto);

        // Настройка mock для сервиса
        Mockito.when(truckService.stackBoxesAndSaveTrucks(any(StackingRequest.class)))
                .thenReturn(mockResponse);

        // JSON-запрос
        String stackingRequestJson = """
            {
                "trucksFileName": "trucks.json",
                "boxesFileName": "boxes.json",
                "typeAlgorithmName": "burke",
                "limitNumberOfTrucks": 5,
                "truckSize": "10x10",
                "boxNames": "один, два, три"
            }
        """;

        // Выполнение POST-запроса и проверка ответа
        mockMvc.perform(post("/trucks/action/stack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stackingRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].truckArea").isArray())
                .andExpect(jsonPath("$[0].truckArea.length()").value(2));
    }
}

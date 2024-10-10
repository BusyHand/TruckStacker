package ru.liga.truckstacker.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.service.BoxService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoxRestController.class)
public class BoxRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoxService boxService;

    @Autowired
    private ObjectMapper objectMapper;

    // Тест для получения всех BoxDto
    @Test
    public void testGetAllBoxes() throws Exception {
        // Создание списка mock-данных
        BoxDto box1 = new BoxDto("один", "1", "1");
        BoxDto box2 = new BoxDto("два", "22", "2");
        List<BoxDto> mockBoxes = Arrays.asList(box1, box2);

        // Настройка поведения сервиса
        Mockito.when(boxService.getAllBoxes()).thenReturn(mockBoxes);

        // Выполнение GET-запроса и проверка результата
        mockMvc.perform(get("/boxes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("один"))
                .andExpect(jsonPath("$[1].name").value("два"));
    }

    // Тест для получения BoxDto по имени
    @Test
    public void testGetBoxByName() throws Exception {
        // Создание mock-данных
        BoxDto mockBox = new BoxDto("один", "1", "1");

        // Настройка поведения сервиса
        Mockito.when(boxService.getBoxByName(anyString())).thenReturn(mockBox);

        // Выполнение GET-запроса и проверка результата
        mockMvc.perform(get("/boxes/{boxName}", "один")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("один"))
                .andExpect(jsonPath("$.form").value("1"))
                .andExpect(jsonPath("$.symbol").value("1"));
    }

    // Тест для создания нового BoxDto
    @Test
    public void testCreateBox() throws Exception {
        // Создание mock-данных
        BoxDto boxDto = new BoxDto("три", "333", "3");

        // Выполнение POST-запроса и проверка результата
        mockMvc.perform(post("/boxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boxDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Successfully created"));

        // Проверка, что метод сервиса был вызван с корректными параметрами
        Mockito.verify(boxService, Mockito.times(1)).createBox(any(BoxDto.class));
    }

    // Тест для обновления BoxDto
    @Test
    public void testUpdateBoxByName() throws Exception {
        // Создание mock-данных
        BoxDto boxDto = new BoxDto("три", "333", "3");

        // Выполнение PUT-запроса и проверка результата
        mockMvc.perform(put("/boxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boxDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully updated"));

        // Проверка, что метод сервиса был вызван с корректными параметрами
        Mockito.verify(boxService, Mockito.times(1)).updateBoxByName(any(BoxDto.class));
    }

    // Тест для удаления BoxDto по имени
    @Test
    public void testDeleteBoxByName() throws Exception {
        // Выполнение DELETE-запроса и проверка результата
        mockMvc.perform(delete("/boxes/{boxName}", "три")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));

        // Проверка, что метод сервиса был вызван с корректными параметрами
        Mockito.verify(boxService, Mockito.times(1)).deleteBoxByName(anyString());
    }
}

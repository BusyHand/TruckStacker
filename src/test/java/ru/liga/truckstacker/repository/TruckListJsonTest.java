package ru.liga.truckstacker.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.model.Truck;
import ru.liga.truckstacker.runner.MainCommandLineRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.liga.truckstacker.config.GlobalSettings.HEIGHT_TRUCK;
import static ru.liga.truckstacker.config.GlobalSettings.WIDTH_TRUCK;

@SpringBootTest
@Import(TestMockBeanConfig.class)
public class TruckListJsonTest {


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testTruckListSerialization() throws JsonProcessingException {
        // �������� ������ TruckDto
        List<Truck> truckDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) { // ������� ��� ������� TruckDto
            int[][] truckArea = new int[HEIGHT_TRUCK][WIDTH_TRUCK];
            for (int j = 0; j < truckArea.length; j++) {
                for (int k = 0; k < truckArea[0].length; k++) {
                    truckArea[j][k] = 1; // ���������� �������
                }
            }
            Truck truckDto = new Truck();
            truckDto.setTruckArea(truckArea);
            truckDtoList.add(truckDto);
        }

        // ������������ ������ TruckDto � JSON
        String json = objectMapper.writeValueAsString(truckDtoList);

        // ��������, ��� JSON �������� ��������� ������
        assertThat(json).isEqualTo("[{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]},{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}]");
    }

    private static final TypeReference<List<Truck>> TRUCK_LIST_TYPE = new TypeReference<>() {
    };

    @Test
    public void testTruckListDeserialization() throws JsonProcessingException {
        // �������� ��������� JSON-������ ������ TruckDto
        String json = "[{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}," +
                "{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}]";
        // �������������� JSON � ������ TruckDto
        List<Truck> truckDtoList = objectMapper.readValue(json, TRUCK_LIST_TYPE);

        // ��������, ��� �������������� ��������� ���������
        assertThat(truckDtoList).isNotNull();
        assertThat(truckDtoList).hasSize(2); // ���������, ��� ������� 2 �������
        for (Truck truckDto : truckDtoList) {
            assertThat(truckDto.getTruckArea()).isNotNull();
            assertThat(truckDto.getTruckArea().length).isEqualTo(HEIGHT_TRUCK); // �������� ������� ���������
            assertThat(truckDto.getTruckArea()[0].length).isEqualTo(WIDTH_TRUCK); // �������� ������� ���������
            for (int[] row : truckDto.getTruckArea()) {
                for (int cell : row) {
                    assertThat(cell).isEqualTo(1);
                }
            }
        }
    }
}

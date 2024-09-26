package ru.liga.truckstacker.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.liga.truckstacker.config.TestMockBeanConfig;
import ru.liga.truckstacker.model.Truck;

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
        List<Truck> truckDtoList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
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

        String json = objectMapper.writeValueAsString(truckDtoList);

        assertThat(json).isEqualTo("[{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]},{\"truckArea\":[[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}]");
    }

    private static final TypeReference<List<Truck>> TRUCK_LIST_TYPE = new TypeReference<>() {
    };

    @Test
    public void testTruckListDeserialization() throws JsonProcessingException {
        String json = "[{\"truckArea\":[[0,0,0,0,0,0],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}," +
                "{\"truckArea\":[[0,0,0,0,0,0],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1],[1,1,1,1,1,1]]}]";
        List<Truck> truckDtoList = objectMapper.readValue(json, TRUCK_LIST_TYPE);

        assertThat(truckDtoList).isNotNull();
        assertThat(truckDtoList).hasSize(2);
        for (Truck truckDto : truckDtoList) {
            int[][] truckArea = truckDto.getTruckArea();
            assertThat(truckArea).isNotNull();
            assertThat(truckArea.length).isEqualTo(HEIGHT_TRUCK);
            assertThat(truckArea[0].length).isEqualTo(WIDTH_TRUCK);
            for (int i = 0; i < truckArea.length; i++) {
                assertThat(truckArea[0][i]).isEqualTo(0);
            }
            for (int i = 1; i < truckArea.length; i++) {
                for (int j = 0; j < truckArea[0].length; j++) {
                    assertThat(truckArea[i][j]).isEqualTo(1);
                }
            }
        }
    }
}

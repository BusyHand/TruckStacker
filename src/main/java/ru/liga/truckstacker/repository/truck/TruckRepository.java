package ru.liga.truckstacker.repository.truck;

import org.springframework.stereotype.Repository;
import ru.liga.truckstacker.model.Truck;

import java.util.List;

public interface TruckRepository {

    List<Truck> save(List<Truck> truckList);
}

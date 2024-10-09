package ru.liga.truckstacker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.truckstacker.entity.Truck;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {

}

package ru.liga.truckstacker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.truckstacker.entity.Box;

public interface BoxRepository extends JpaRepository<Box, String> {


}

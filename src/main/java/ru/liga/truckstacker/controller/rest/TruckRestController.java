package ru.liga.truckstacker.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.truckstacker.dto.StackingRequest;
import ru.liga.truckstacker.dto.TruckDto;
import ru.liga.truckstacker.service.TruckService;

import java.util.List;

@RestController
@RequestMapping("/trucks")
@RequiredArgsConstructor
public class TruckRestController {

    private final TruckService truckService;

    @PostMapping("/action/stack")
    public ResponseEntity<List<TruckDto>> stackBoxesAndSaveTrucks(@RequestBody StackingRequest stackingRequest) {
        List<TruckDto> trucks = truckService.stackBoxesAndSaveTrucks(stackingRequest);
        return new ResponseEntity<>(trucks, HttpStatus.CREATED);
    }
}
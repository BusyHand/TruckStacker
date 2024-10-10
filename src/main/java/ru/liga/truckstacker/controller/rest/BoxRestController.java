package ru.liga.truckstacker.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.liga.truckstacker.dto.BoxDto;
import ru.liga.truckstacker.service.BoxService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/boxes")
@RequiredArgsConstructor
public class BoxRestController {

    private final BoxService boxService;

    @GetMapping
    public ResponseEntity<List<BoxDto>> getAllBoxes() {
        List<BoxDto> boxes = boxService.getAllBoxes();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/{boxName}")
    public ResponseEntity<BoxDto> getBoxByName(@PathVariable String boxName) {
        BoxDto boxDto = boxService.getBoxByName(boxName);
        return ResponseEntity.ok(boxDto);
    }

    @PostMapping
    public ResponseEntity<String> createBox(@Valid @RequestBody BoxDto boxDto) {
        boxService.createBox(boxDto);
        return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateBoxByName(@Valid @RequestBody BoxDto boxDto) {
        boxService.updateBoxByName(boxDto);
        return new ResponseEntity<>("Successfully updated", HttpStatus.OK);
    }

    @DeleteMapping("/{boxName}")
    public ResponseEntity<String> deleteBoxByName(@PathVariable String boxName) {
        boxService.deleteBoxByName(boxName);
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }
}
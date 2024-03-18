package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Period;
import org.massmanagement.service.PeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/period/v1")
@RequiredArgsConstructor
public class PeriodController {
    private final PeriodService periodService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Period period) {
        var saved = periodService.save(period);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var period = periodService.getById(id);
        return ResponseEntity.ok(period);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var periods = periodService.getAll();
        return ResponseEntity.ok(periods);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = periodService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Period deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete period."));
    }
}

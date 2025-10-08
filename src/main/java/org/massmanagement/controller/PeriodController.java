package org.massmanagement.controller;

import org.massmanagement.model.Period;
import org.massmanagement.service.PeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/period/v1")
class PeriodController {
    private final PeriodService periodService;

    PeriodController(PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Period period) {
        var saved = periodService.save(period);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        var period = periodService.getById(id);
        return ResponseEntity.ok(period);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var periods = periodService.getAll();
        return ResponseEntity.ok(periods);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = periodService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Period deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                        "Could not delete period. Make sure period is not in use."));
    }
}

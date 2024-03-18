package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Cost;
import org.massmanagement.service.CostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/cost/v1")
@RequiredArgsConstructor
public class CostController {
    private final CostService costService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Cost cost) {
        var saved = costService.save(cost);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var cost = costService.getById(id);
        return ResponseEntity.ok(cost);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var costList = costService.getAll();
        return ResponseEntity.ok(costList);
    }

    @GetMapping("/get-all-by-type/{type}")
    public ResponseEntity<?> getAllByType(@PathVariable long type) {
        var costList = costService.getAllByType(type);
        return ResponseEntity.ok(costList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = costService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Cost deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete cost."));
    }
}

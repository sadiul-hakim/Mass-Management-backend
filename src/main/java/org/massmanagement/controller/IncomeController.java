package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Income;
import org.massmanagement.service.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/income/v1")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Income income) {
        var saved = incomeService.save(income);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var income = incomeService.getById(id);
        return ResponseEntity.ok(income);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var incomeList = incomeService.getAll();
        return ResponseEntity.ok(incomeList);
    }

    @GetMapping("/get-all-by-type/{type}")
    public ResponseEntity<?> getAllByType(@PathVariable long type) {
        var incomeList = incomeService.getAllByType(type);
        return ResponseEntity.ok(incomeList);
    }

    @GetMapping("/get-all-by-user/{user}")
    public ResponseEntity<?> getAllByUser(@PathVariable long user) {
        var incomeList = incomeService.getAllByUser(user);
        return ResponseEntity.ok(incomeList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = incomeService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Income deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete income."));
    }
}

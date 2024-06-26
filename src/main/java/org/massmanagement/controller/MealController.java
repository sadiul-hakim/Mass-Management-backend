package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Meal;
import org.massmanagement.model.MealInRange;
import org.massmanagement.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meal/v1")
@RequiredArgsConstructor
class MealController {
    private final MealService mealService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody Meal meal) {
        var savedMeal = mealService.save(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMeal);
    }

    @PostMapping(value = "/add-in-range", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addInRange(@RequestBody MealInRange mealInRange) {
        var savedMeal = mealService.saveInRange(mealInRange);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMeal);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        var meal = mealService.getById(id);
        return ResponseEntity.ok(meal);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var mealList = mealService.getAll();
        return ResponseEntity.ok(mealList);
    }

    @GetMapping(value = "/get-all-by-user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllByUser(@PathVariable long userId) {
        var mealList = mealService.getAllByUser(userId);
        return ResponseEntity.ok(mealList);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = mealService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Meal deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete meal."));
    }

    @GetMapping(value = "/get-meal-sheet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> mealSheet() {
        Map<String, Map<String, List<Double>>> mealSheet = mealService.mealSheet();
        return ResponseEntity.ok(mealSheet);
    }
}

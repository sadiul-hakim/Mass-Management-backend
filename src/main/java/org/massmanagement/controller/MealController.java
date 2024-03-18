package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.Meal;
import org.massmanagement.service.MealService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/meal/v1")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Meal meal) {
        var savedMeal = mealService.save(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMeal);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var meal = mealService.getById(id);
        return ResponseEntity.ok(meal);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var mealList = mealService.getAll();
        return ResponseEntity.ok(mealList);
    }

    @GetMapping("/get-all-by-user/{userId}")
    public ResponseEntity<?> getAllByUser(@PathVariable long userId) {
        var mealList = mealService.getAllByUser(userId);
        return ResponseEntity.ok(mealList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = mealService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Meal deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete meal."));
    }
}

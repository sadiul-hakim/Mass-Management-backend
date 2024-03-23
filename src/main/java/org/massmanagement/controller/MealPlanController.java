package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.MealPlan;
import org.massmanagement.service.MealPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meal-plan/v1")
public class MealPlanController {
    private final MealPlanService mealPlanService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody MealPlan mealPlan) {
        var saved = mealPlanService.save(mealPlan);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var mealPlan = mealPlanService.getById(id);
        return ResponseEntity.ok(mealPlan);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var mealPlans = mealPlanService.getAll();
        return ResponseEntity.ok(mealPlans);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = mealPlanService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Plan deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete plan."));
    }
}

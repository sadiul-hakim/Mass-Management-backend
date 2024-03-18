package org.massmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.massmanagement.model.MealType;
import org.massmanagement.service.MealTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/meal-type/v1")
@RequiredArgsConstructor
public class MealTypeController {
    private final MealTypeService mealTypeService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody MealType mealType) {
        var savedType = mealTypeService.save(mealType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedType);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        var mealType = mealTypeService.getById(id);
        return ResponseEntity.ok(mealType);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        var mealTypeList = mealTypeService.getAll();
        return ResponseEntity.ok(mealTypeList);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = mealTypeService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Meal Type deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Could not delete meal type."));
    }
}

package org.massmanagement.controller;

import org.massmanagement.model.MealType;
import org.massmanagement.service.MealTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/meal-type/v1")
class MealTypeController {
    private final MealTypeService mealTypeService;

    MealTypeController(MealTypeService mealTypeService) {
        this.mealTypeService = mealTypeService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> add(@RequestBody MealType mealType) {
        var savedType = mealTypeService.save(mealType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedType);
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable long id) {
        var mealType = mealTypeService.getById(id);
        return ResponseEntity.ok(mealType);
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        var mealTypeList = mealTypeService.getAll();
        return ResponseEntity.ok(mealTypeList);
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        boolean deleted = mealTypeService.delete(id);
        return deleted ? ResponseEntity.ok(Collections.singletonMap("message", "Meal Type deleted successfully.")) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                        "Could not delete meal type. Make sure type is not in use."));
    }
}

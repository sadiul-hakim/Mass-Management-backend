package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.Meal;
import org.massmanagement.repository.MealRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepo mealRepo;
    public Meal save(Meal meal) {
        log.info("Saving meal : {}", meal);
        return mealRepo.save(meal);
    }

    public Meal getById(long id) {
        log.info("Getting meal by id : {}", id);
        return mealRepo.findById(id).orElse(new Meal());
    }

    public List<Meal> getAll() {
        log.info("Getting all meals.");
        return mealRepo.findAll();
    }

    public List<Meal> getAllByUser(long user) {
        log.info("Getting all meals by user {}.", user);
        return mealRepo.findAllByUserId(user);
    }

    public boolean delete(long id) {
        log.info("Deleting meal by id : {}", id);
        try {
            mealRepo.deleteById(id);
            log.info("Meal {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.MealType;
import org.massmanagement.repository.MealTypeRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealTypeService {
    private final MealTypeRepo mealTypeRepo;

    public MealType save(MealType mealType) {
        log.info("Saving Meal Type : {}", mealType);
        return mealTypeRepo.save(mealType);
    }

    public MealType getById(long id) {
        log.info("Getting meal type by id : {}", id);
        return mealTypeRepo.findById(id).orElse(new MealType());
    }

    public List<MealType> getAll() {
        log.info("Getting all meal types.");
        return mealTypeRepo.findAll();
    }

    public boolean delete(long id) {
        log.info("Deleting meal type id : {}", id);
        try {
            mealTypeRepo.deleteById(id);
            log.info("Meal type {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

package org.massmanagement.service;

import org.massmanagement.model.MealType;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.repository.MealTypeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealTypeService {
    private final MealTypeRepo mealTypeRepo;
    private final MealRepo mealRepo;

    private static final Logger log = LoggerFactory.getLogger(MealTypeService.class);

    public MealTypeService(MealTypeRepo mealTypeRepo, MealRepo mealRepo) {
        this.mealTypeRepo = mealTypeRepo;
        this.mealRepo = mealRepo;
    }

    public MealType save(MealType mealType) {
        log.info("Saving Meal Type : {}", mealType);

        if (mealType.getName().isEmpty()) {
            log.warn("Invalid meal type!");
            log.info(mealType.toString());
            return new MealType();
        }

        Optional<MealType> type = mealTypeRepo.findByName(mealType.getName());
        if (type.isPresent()) {
            log.warn("Meal Type already exists!");
            return new MealType();
        }

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

            if (!mealRepo.findAllByType(id).isEmpty()) {
                log.warn("Meal Type is in use!");
                return false;
            }

            mealTypeRepo.deleteById(id);
            log.info("Meal type {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

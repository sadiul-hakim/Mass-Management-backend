package org.massmanagement.service;

import org.massmanagement.dto.MealPlanDTO;
import org.massmanagement.model.MealPlan;
import org.massmanagement.repository.MealPlanRepo;
import org.massmanagement.util.DateFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealPlanService {
    private final MealPlanRepo mealPlanRepo;
    private final PeriodService periodService;

    private static final Logger log = LoggerFactory.getLogger(MealPlanService.class);

    public MealPlanService(MealPlanRepo mealPlanRepo, PeriodService periodService) {
        this.mealPlanRepo = mealPlanRepo;
        this.periodService = periodService;
    }

    public MealPlanDTO save(MealPlan meal) {
        log.info("Saving meal : {}", meal);

        if (meal.getItem().isEmpty() || meal.getPeriod() == 0) {
            log.warn("Trying to save invalid meal plan!");
            log.info(meal.getItem());
            return new MealPlanDTO();
        }
        return convertToDTO(mealPlanRepo.save(meal));
    }

    public MealPlanDTO getById(long id) {
        log.info("Getting meal by id : {}", id);
        return convertToDTO(mealPlanRepo.findById(id).orElse(new MealPlan()));
    }

    public List<MealPlanDTO> getAll() {
        log.info("Getting all meals.");
        var all = mealPlanRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public boolean delete(long id) {
        log.info("Deleting meal by id : {}", id);
        try {
            mealPlanRepo.deleteById(id);
            log.info("Meal {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public MealPlanDTO convertToDTO(MealPlan mealPlan) {
        if (mealPlan == null) new MealPlanDTO();

        var period = periodService.getById(mealPlan.getPeriod());
        return new MealPlanDTO(mealPlan.getId(), mealPlan.getItem(), period, DateFormatter.formatDate(mealPlan.getDate()));
    }
}

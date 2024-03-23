package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.MealPlanDTO;
import org.massmanagement.model.MealPlan;
import org.massmanagement.repository.MealPlanRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealPlanService {
    private final MealPlanRepo mealPlanRepo;
    private final PeriodService periodService;
    public MealPlanDTO save(MealPlan meal) {
        log.info("Saving meal : {}", meal);
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

    public MealPlanDTO convertToDTO(MealPlan mealPlan){
        var period = periodService.getById(mealPlan.getPeriod());
        return new MealPlanDTO(mealPlan.getId(), mealPlan.getItem(),period, DateFormatter.formatDate(mealPlan.getDate()));
    }
}

package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.MealDTO;
import org.massmanagement.model.Meal;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepo mealRepo;
    private final UserService userService;
    private final MealTypeService mealTypeService;
    private final PeriodService periodService;
    public MealDTO save(Meal meal) {
        log.info("Saving meal : {}", meal);
        return convertToDTO(mealRepo.save(meal));
    }

    public MealDTO getById(long id) {
        log.info("Getting meal by id : {}", id);
        return convertToDTO(mealRepo.findById(id).orElse(new Meal()));
    }

    public List<MealDTO> getAll() {
        log.info("Getting all meals.");
        var all = mealRepo.findAll();
        return all.stream().map(this::convertToDTO).toList();
    }

    public List<MealDTO> getAllByUser(long user) {
        log.info("Getting all meals by user {}.", user);
        var all = mealRepo.findAllByUserId(user);
        return all.stream().map(this::convertToDTO).toList();
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

    public MealDTO convertToDTO(Meal meal){
        var user = userService.getById(meal.getUserId());
        var type = mealTypeService.getById(meal.getType());
        var period = periodService.getById(meal.getPeriod());

        return new MealDTO(meal.getId(),user,type,meal.getAmount(), DateFormatter.formatDate(meal.getDate()),period);
    }
}

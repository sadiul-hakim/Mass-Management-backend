package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.dto.MealDTO;
import org.massmanagement.model.Meal;
import org.massmanagement.model.MealInRange;
import org.massmanagement.model.Period;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (meal.getAmount() == 0 || meal.getType() == 0 || meal.getPeriod() == 0 || meal.getUserId() == 0) {
            log.warn("Trying to save invalid meal!");
            log.info(meal.toString());
            return null;
        }
        return convertToDTO(mealRepo.save(meal));
    }

    public boolean saveInRange(MealInRange mealInRange) {
        log.info("Saving meal : {}", mealInRange);

        if (mealInRange.getAmount() == 0 || mealInRange.getType() == 0 || mealInRange.getUserId() == 0) {
            log.warn("Trying to save invalid meal!");
            log.info(mealInRange.toString());
            return false;
        }

        List<Meal> meals = generateMeals(mealInRange);
        meals.forEach(this::save);

        return true;
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

    public List<MealDTO> getAllByType(long type) {
        log.info("Getting all meals by type {}.", type);
        var all = mealRepo.findAllByType(type);
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

    public boolean deleteAll() {
        log.info("Deleting all meals.");
        try {
            mealRepo.deleteAll();
            return true;
        } catch (Exception ex) {
            log.error("Could not delete meals. Cause {}", ex.getMessage());
            return false;
        }
    }

    private List<Meal> generateMeals(MealInRange mealInRange) {
        LocalDateTime startDate = mealInRange.getStartDate().toLocalDateTime();
        LocalDateTime endDate = mealInRange.getEndDate().toLocalDateTime();
        endDate = endDate.plusDays(1);

        List<Meal> meals = new ArrayList<>();
        if (mealInRange.getPeriod() == 0) {
            List<Period> periods = periodService.getAll();
            for (Period period : periods) {

                List<Meal> mealList = generateMealByPeriod(period, startDate, mealInRange, endDate);
                meals.addAll(mealList);
            }
        } else {
            Period period = periodService.getById(mealInRange.getPeriod());
            List<Meal> mealList = generateMealByPeriod(period, startDate, mealInRange, endDate);
            meals.addAll(mealList);
        }

        return meals;
    }

    private List<Meal> generateMealByPeriod(Period period, LocalDateTime startDate, MealInRange mealInRange, LocalDateTime endDate) {
        List<Meal> meals = new ArrayList<>();

        LocalDateTime startDateCopy = startDate;
        while (startDateCopy.isBefore(endDate)) {
            Meal meal = new Meal(0, mealInRange.getUserId(), mealInRange.getType(),
                    mealInRange.getAmount(), Timestamp.valueOf(startDateCopy), period.getId());
            meals.add(meal);
            startDateCopy = startDateCopy.plusDays(1);
        }

        return meals;
    }

    public MealDTO convertToDTO(Meal meal) {
        var user = userService.getById(meal.getUserId());
        var type = mealTypeService.getById(meal.getType());
        var period = periodService.getById(meal.getPeriod());

        return new MealDTO(meal.getId(), user, type, meal.getAmount(), DateFormatter.formatDate(meal.getDate()), period);
    }
}

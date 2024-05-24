package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.MealPlan;
import org.massmanagement.model.Period;
import org.massmanagement.repository.MealPlanRepo;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.repository.PeriodRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeriodService {
    private final PeriodRepo periodRepo;
    private final MealRepo mealRepo;
    private final MealPlanRepo mealPlanRepo;

    public Period save(Period period) {
        log.info("Saving period : {}", period);

        if (period.getName().isEmpty()) {
            log.warn("Invalid period!");
            log.info(period.toString());
            return null;
        }
        return periodRepo.save(period);
    }

    public Period getById(long id) {
        log.info("Getting period by id : {}", id);
        return periodRepo.findById(id).orElse(new Period());
    }

    public List<Period> getAll() {
        log.info("Getting all periods.");
        return periodRepo.findAll();
    }

    public long count() {
        log.info("Counting periods.");
        return periodRepo.count();
    }

    public boolean delete(long id) {
        log.info("Deleting period id : {}", id);
        try {

            if (!(mealRepo.findAllByPeriod(id).isEmpty() && mealPlanRepo.findAllByPeriod(id).isEmpty())) {
                log.warn("Period is in use!");
                return false;
            }

            periodRepo.deleteById(id);
            log.info("Period {} deleted successfully.", id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

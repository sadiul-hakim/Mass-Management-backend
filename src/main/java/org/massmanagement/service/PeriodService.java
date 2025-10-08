package org.massmanagement.service;

import org.massmanagement.model.Period;
import org.massmanagement.repository.MealPlanRepo;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.repository.PeriodRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeriodService {
    private final PeriodRepo periodRepo;
    private final MealRepo mealRepo;
    private final MealPlanRepo mealPlanRepo;

    private static final Logger log = LoggerFactory.getLogger(PeriodService.class);

    public PeriodService(PeriodRepo periodRepo, MealRepo mealRepo, MealPlanRepo mealPlanRepo) {
        this.periodRepo = periodRepo;
        this.mealRepo = mealRepo;
        this.mealPlanRepo = mealPlanRepo;
    }

    public Period save(Period period) {
        log.info("Saving period : {}", period);

        if (period.getName().isEmpty()) {
            log.warn("Invalid period!");
            log.info(period.toString());
            return new Period();
        }

        Optional<Period> existingPeriod = periodRepo.findByPeriodOrderOrName(period.getPeriodOrder(), period.getName());
        if (existingPeriod.isPresent()) {
            log.warn("Period with this order already exists!");
            return new Period();
        }

        return periodRepo.save(period);
    }

    public Period getById(long id) {
        log.info("Getting period by id : {}", id);
        return periodRepo.findById(id).orElse(new Period());
    }

    public Period getByOrder(long order) {
        log.info("Getting period by order : {}", order);
        return periodRepo.findPeriodByPeriodOrder(order).orElse(new Period());
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

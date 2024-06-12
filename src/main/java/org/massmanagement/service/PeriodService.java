package org.massmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.massmanagement.model.MealPlan;
import org.massmanagement.model.Period;
import org.massmanagement.repository.MealPlanRepo;
import org.massmanagement.repository.MealRepo;
import org.massmanagement.repository.PeriodRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<Period> existingPeriod = periodRepo.findByPeriodOrderOrName(period.getPeriodOrder(), period.getName());
        if (existingPeriod.isPresent()) {
            log.warn("Period with this order already exists!");
            return null;
        }

        clearCache();
        return periodRepo.save(period);
    }

    @Cacheable("Period:getById")
    public Period getById(long id) {
        log.info("Getting period by id : {}", id);
        return periodRepo.findById(id).orElse(new Period());
    }

    @Cacheable("Period:getByOrder")
    public Period getByOrder(long order) {
        log.info("Getting period by order : {}", order);
        return periodRepo.findPeriodByPeriodOrder(order).orElse(new Period());
    }

    @Cacheable("Period:getAll")
    public List<Period> getAll() {
        log.info("Getting all periods.");
        return periodRepo.findAll();
    }

    @Cacheable("Period:count")
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

    @CacheEvict(value = {"Period:getById", "Period:getAll", "Period:count", "Period:getByOrder"}, allEntries = true)
    public void clearCache() {
        log.info("Cleared all Period Cache!");
    }
}

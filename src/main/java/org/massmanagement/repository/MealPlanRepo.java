package org.massmanagement.repository;

import org.massmanagement.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanRepo extends JpaRepository<MealPlan,Long> {
    List<MealPlan> findAllByPeriod(long period);
}

package org.massmanagement.repository;

import org.massmanagement.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanRepo extends JpaRepository<MealPlan,Long> {
}

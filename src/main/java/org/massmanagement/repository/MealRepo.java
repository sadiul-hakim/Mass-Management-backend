package org.massmanagement.repository;

import org.massmanagement.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepo extends JpaRepository<Meal,Long> {
    List<Meal> findAllByUserId(long userId);
}

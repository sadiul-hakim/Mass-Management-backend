package org.massmanagement.repository;

import org.massmanagement.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealTypeRepo extends JpaRepository<MealType,Long> {
}

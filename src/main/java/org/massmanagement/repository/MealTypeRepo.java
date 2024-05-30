package org.massmanagement.repository;

import org.massmanagement.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealTypeRepo extends JpaRepository<MealType,Long> {
    Optional<MealType> findByName(String name);
}

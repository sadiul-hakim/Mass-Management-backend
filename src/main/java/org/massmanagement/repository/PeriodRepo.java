package org.massmanagement.repository;

import org.massmanagement.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepo extends JpaRepository<Period,Long> {
}

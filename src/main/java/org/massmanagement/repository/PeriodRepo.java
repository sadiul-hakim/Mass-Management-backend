package org.massmanagement.repository;

import org.massmanagement.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PeriodRepo extends JpaRepository<Period, Long> {
    @Query(value = "select count(*) from Period")
    long findCountOfPeriod();

    Optional<Period> findByPeriodOrderOrName(int periodOrder,String name);
}

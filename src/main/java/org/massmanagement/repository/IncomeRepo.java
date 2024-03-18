package org.massmanagement.repository;

import org.massmanagement.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepo extends JpaRepository<Income,Long> {
    List<Income> findAllByType(long type);
    List<Income> findAllByUserId(long userId);
}

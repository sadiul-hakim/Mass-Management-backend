package org.massmanagement.repository;

import org.massmanagement.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface IncomeRepo extends JpaRepository<Income, Long> {
    List<Income> findAllByType(long type);

    List<Income> findAllByUserId(long userId);

    @Query(value = "SELECT SUM(amount) FROM income", nativeQuery = true)
    long findSumOfAmount();

    @Query(value = "SELECT TYPE FROM income", nativeQuery = true)
    Set<Long> findCountOfType();

    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM income WHERE user_id = :userId AND TYPE = :type", nativeQuery = true)
    Long findSumOfIncomeByUserIdAndType(@Param("userId") long userId, @Param("type") long type);
}

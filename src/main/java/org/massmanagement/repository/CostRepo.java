package org.massmanagement.repository;

import org.massmanagement.model.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CostRepo extends JpaRepository<Cost,Long> {
    List<Cost> findAllByType(long type);
    @Query(value = "SELECT SUM(amount) FROM cost",nativeQuery = true)
    long findSumOfAmount();

    @Query(value = "SELECT SUM(amount) FROM cost where type = :type",nativeQuery = true)
    long findSumOfAmountByType(@Param("type") long type);
    List<Cost> findByTypeNotIn(List<Long> type);
}

package org.massmanagement.repository;

import org.massmanagement.model.Cost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CostRepo extends JpaRepository<Cost,Long> {
    List<Cost> findAllByType(long type);
}
